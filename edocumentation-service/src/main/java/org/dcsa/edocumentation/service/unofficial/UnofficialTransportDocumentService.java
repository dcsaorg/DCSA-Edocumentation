package org.dcsa.edocumentation.service.unofficial;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.PartyService;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;
import org.dcsa.edocumentation.transferobjects.unofficial.DraftTransportDocumentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.TransportDocumentRefStatusTO;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.repository.CarrierRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialTransportDocumentService {

  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final CarrierRepository carrierRepository;

  private final PartyService partyService;
  private final TransportDocumentMapper transportDocumentMapper;
  private final TransportDocumentRepository transportDocumentRepository;

  private final DocumentStatusMapper documentStatusMapper;
  private final Validator validator;

  @Transactional
  public Optional<TransportDocumentRefStatusTO> issueDraft(DraftTransportDocumentRequestTO transportDocumentRequestTO) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(transportDocumentRequestTO.shippingInstructionReference())
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    ValidationResult<EblDocumentStatus> validationResult;
    try {
      validationResult = shippingInstruction.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict("SI is not in a state to be DRFT'ed", e);
    }
    if (!validationResult.validationErrors().isEmpty()) {
      var status = validationResult.proposedStatus();
      var reason = validationResult.presentErrors(5000);
      ShipmentEvent e;
      if (status == EblDocumentStatus.REJE) {
        e = shippingInstruction.rejected(reason);
      } else {
        assert status == EblDocumentStatus.PENU;
        e = shippingInstruction.pendingUpdate(reason);
      }
      shippingInstructionRepository.save(shippingInstruction);
      shipmentEventRepository.save(e);

      throw ConcreteRequestErrorMessageException.conflict(
        "The SI had validation errors and is now in state " + status.name(),
        null
      );
    }

    String carrierSMDGCode = findCarrierSMGCode(transportDocumentRequestTO.issuingParty());
    Carrier carrier = carrierRepository.findBySmdgCode(carrierSMDGCode);
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.notFound("Unknown carrier SMDG code: " + carrierSMDGCode);
    }
    // FIXME: We need to "freeze" the transport document such that changes to the SI does *NOT* change
    //  the transport document. This affects basically any non-trivial object associated with the
    //  transport document.
    // TODO: This logic should not be a builder; but in a SI .issueDraft() or something like that, which could
    //  then validate the draft document is in a sensible state (e.g., isElectronic vs. original/copies/rider pages)
    TransportDocument document = TransportDocument.builder()
      .shippingInstruction(shippingInstruction)
      .shippedOnBoardDate(shippingInstruction.getIsShippedOnBoardType() == Boolean.TRUE ? transportDocumentRequestTO.shipmentDate() : null)
      .receivedForShipmentDate(shippingInstruction.getIsShippedOnBoardType() == Boolean.FALSE ? transportDocumentRequestTO.shipmentDate() : null)
      .numberOfRiderPages(transportDocumentRequestTO.numberOfRiderPages())
      .carrier(carrier)
      .issuingParty(partyService.createParty(transportDocumentRequestTO.issuingParty()))
      .build();

    shipmentEventRepository.save(document.draft());
    transportDocumentRepository.save(document);

    return Optional.of(transportDocumentMapper.toStatusDTO(document));
  }

  private static String findCarrierSMGCode(PartyTO issuingParty) {
    var identifyingCodes = issuingParty.identifyingCodes();
    String partyCode = null;
    if (identifyingCodes != null) {
      var carrierCodes = identifyingCodes.stream()
        .filter(code -> DCSAResponsibleAgencyCode.SMDG.equals(code.dcsaResponsibleAgencyCode()))
        .filter(code -> Objects.equals(code.codeListName(), "LCL"))
        .map(PartyIdentifyingCodeTO::partyCode)
        .toList();
      if (carrierCodes.size() == 1) {
        partyCode = carrierCodes.get(0);
      }
    }
    if (partyCode == null) {
      throw ConcreteRequestErrorMessageException.invalidInput("issuingParty must have exactly ONE SMDG party code with codeListName set to LCL");
    }
    return partyCode;
  }

  public Optional<TransportDocumentRefStatusTO> changeState(
    String transportDocumentReference,
    org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus status,
    String reason
  ) {
    TransportDocument transportDocument = transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(transportDocumentReference)
      .orElse(null);
    if (transportDocument == null) {
      return Optional.empty();
    }
    ShipmentEvent event = switch (documentStatusMapper.toDomainEblDocumentStatus(status)) {
      case APPR -> transportDocument.approve();
      case PENA -> transportDocument.pendingApproval(reason);
      case ISSU -> transportDocument.issue();
      case SURR -> transportDocument.surrender();
      case VOID -> transportDocument.voidDocument();
      case DRFT -> throw ConcreteRequestErrorMessageException.invalidInput("Please use the issueDraft endpoint instead!");
      default -> throw ConcreteRequestErrorMessageException.invalidInput("Cannot go to state " + status);
    };

    shipmentEventRepository.save(event);
    // Note this only works for cases where we can update the documentStatus in-place.
    transportDocument = transportDocumentRepository.save(transportDocument);
    return Optional.of(transportDocumentMapper.toStatusDTO(transportDocument));
  }
}
