package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.CarrierRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.EBLValidation;
import org.dcsa.edocumentation.domain.validations.PaperBLValidation;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.PartyService;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;
import org.dcsa.edocumentation.transferobjects.unofficial.DraftTransportDocumentRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialTransportDocumentService {

  private final ShippingInstructionRepository shippingInstructionRepository;
  private final CarrierRepository carrierRepository;

  private final PartyService partyService;
  private final TransportDocumentMapper transportDocumentMapper;
  private final TransportDocumentRepository transportDocumentRepository;
  private final UnofficialShippingInstructionService unofficialShippingInstructionService;

  private final Validator validator;

  @Transactional
  public Optional<TransportDocumentRefStatusTO> issueDraft(DraftTransportDocumentRequestTO transportDocumentRequestTO) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(transportDocumentRequestTO.shippingInstructionReference())
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    if (!unofficialShippingInstructionService.performValidationOfShippingInstruction(shippingInstruction)) {
      throw ConcreteRequestErrorMessageException.conflict(
        "The SI had validation errors; please run the validation endpoint to trigger the relevant shipment event",
        null
      );
    }

    String carrierSMDGCode = findCarrierSMGCode(transportDocumentRequestTO.issuingParty());
    var carrier = carrierRepository.findBySmdgCode(carrierSMDGCode);
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.notFound("Unknown carrier SMDG code: " + carrierSMDGCode);
    }

    boolean shouldHaveDeclaredValue = shippingInstruction.getConsignmentItems()
      .stream()
      .map(ConsignmentItem::getConfirmedBooking)
      .map(ConfirmedBooking::getBooking)
      .map(Booking::getDeclaredValue)
      .anyMatch(Objects::nonNull);

    if (shouldHaveDeclaredValue && transportDocumentRequestTO.declaredValue() == null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "Please provide declaredValue + declaredValueCurrency for this transport document.  At least one of the"
          + " bookings have declared value and the reference implementation cannot determine how much of it"
          + " should end up in this transport document (vs. other transport documents)"
      );
    } else if (!shouldHaveDeclaredValue && transportDocumentRequestTO.declaredValue() == null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "Please do not provide declaredValue + declaredValueCurrency for this transport document. None of the"
          + " related bookings had a declared value."
      );
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
      .declaredValueCurrency(transportDocumentRequestTO.declaredValueCurrency())
      .declaredValue(transportDocumentRequestTO.declaredValue())
      .build();

    TransportDocumentTO mapped = transportDocumentMapper.toDTO(document);
    Class<?>[] validations = {
      AsyncShipperProvidedDataValidation.class,
      (mapped.isElectronic() == Boolean.TRUE ? EBLValidation.class : PaperBLValidation.class),
    };
    var errors = validator.validate(mapped, validations);
    if (!errors.isEmpty()) {
      var r =
          new ValidationResult<>(
              BookingStatus.PENDING_UPDATES_CONFIRMATION,
              errors.stream()
                  .map(v -> v.getPropertyPath().toString() + ": " + v.getMessage())
                  .toList());
      throw new AssertionError("Generated draft TD had validation errors. " + r.presentErrors(Integer.MAX_VALUE));
    }

    document.draft();
    transportDocumentRepository.save(document);

    return Optional.of(transportDocumentMapper.toStatusDTO(document));
  }

  private static String findCarrierSMGCode(PartyTO issuingParty) {
    var identifyingCodes = issuingParty.identifyingCodes();
    String partyCode = null;
    if (identifyingCodes != null) {
      var carrierCodes = identifyingCodes.stream()
        .filter(code -> DCSAResponsibleAgencyCode.SMDG.name().equals(code.dcsaResponsibleAgencyCode()))
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
    String documentStatus
  ) {
    TransportDocument transportDocument = transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(transportDocumentReference)
      .orElse(null);
    if (transportDocument == null) {
      return Optional.empty();
    }

    switch (documentStatus) {
      case EblDocumentStatus.APPROVED -> transportDocument.approveFromCarrier();
      case EblDocumentStatus.PENDING_APPROVAL -> transportDocument.pendingApproval();
      // FIXME: issue requires that all documents related to the booking is issued at the same time
      //  That is, if booking is split between multiple SIs, the TDs for those SIs must be issued at the same time
      case EblDocumentStatus.ISSUED -> transportDocument.issue();
      case EblDocumentStatus.SURRENDERED -> transportDocument.surrender();
      case EblDocumentStatus.VOID -> transportDocument.voidDocument();
      case EblDocumentStatus.DRAFT -> throw ConcreteRequestErrorMessageException.invalidInput("Please use the issueDraft endpoint instead!");
      default -> throw ConcreteRequestErrorMessageException.invalidInput("Cannot go to state " + documentStatus);
    }

    // Note this only works for cases where we can update the documentStatus in-place.
    transportDocument = transportDocumentRepository.save(transportDocument);
    return Optional.of(transportDocumentMapper.toStatusDTO(transportDocument));
  }
}
