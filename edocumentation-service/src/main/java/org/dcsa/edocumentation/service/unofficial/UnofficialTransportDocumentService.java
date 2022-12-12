package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.PartyService;
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

import javax.transaction.Transactional;
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

  @Transactional
  public Optional<TransportDocumentRefStatusTO> issueDraft(DraftTransportDocumentRequestTO transportDocumentRequestTO) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(transportDocumentRequestTO.shippingInstructionReference())
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    String carrierSMDGCode = findCarrierSMGCode(transportDocumentRequestTO.issuingParty());
    Carrier carrier = carrierRepository.findBySmdgCode(carrierSMDGCode);
    if (carrier == null) {
      throw ConcreteRequestErrorMessageException.notFound("Unknown carrier SMDG code: " + carrierSMDGCode);
    }
    TransportDocument document = TransportDocument.builder()
      .shippingInstruction(shippingInstruction)
      .shippedOnBoardDate(transportDocumentRequestTO.shippedOnBoardDate())
      .receivedForShipmentDate(transportDocumentRequestTO.receivedForShipmentDate())
      .numberOfRiderPages(transportDocumentRequestTO.numberOfRiderPages())
      // FIXME: numberOfOriginalsWithCharges
      // FIXME: numberOfOriginalsWithoutCharges
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
}
