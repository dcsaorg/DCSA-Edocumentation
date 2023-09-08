package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {

  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;
  private final DocumentPartyService documentPartyService;
  private final ReferenceService referenceService;
  private final UtilizedTransportEquipmentService utilizedTransportEquipmentService;
  private final StuffingService stuffingService;
  private final DisplayedAddressMapper displayedAddressMapper;

  @Transactional
  public Optional<ShippingInstructionTO> findByReference(String shippingInstructionReference) {
    return shippingInstructionRepository
        .findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
        .map(shippingInstructionMapper::toDTO);
  }

  @Transactional
  public ShippingInstructionRefStatusTO createShippingInstruction(
      ShippingInstructionTO shippingInstructionTO) {

    ShippingInstruction shippingInstruction = toDAOBuilder(shippingInstructionTO).build();
    shipmentEventRepository.save(shippingInstruction.receive());

    ShippingInstruction updatedShippingInstruction =
        shippingInstructionRepository.save(shippingInstruction);
    documentPartyService.createDocumentParties(
        shippingInstructionTO.documentParties(), updatedShippingInstruction);
    referenceService.createReferences(
        shippingInstructionTO.references(), updatedShippingInstruction);
    Map<String, UtilizedTransportEquipment> savedUtilizedTransportEquipments =
        utilizedTransportEquipmentService.createUtilizedTransportEquipment(
            shippingInstructionTO.utilizedTransportEquipments());
    stuffingService.createStuffing(
        updatedShippingInstruction,
        savedUtilizedTransportEquipments,
        shippingInstructionTO.consignmentItems());
    return shippingInstructionMapper.toStatusDTO(updatedShippingInstruction);
  }

  // Return a builder because PUT will need to copy in some extra fields.
  private ShippingInstruction.ShippingInstructionBuilder toDAOBuilder(
      ShippingInstructionTO shippingInstructionTO) {
    return shippingInstructionMapper.toDAO(shippingInstructionTO).toBuilder()
        .displayedNameForPlaceOfReceipt(
            displayedAddressMapper.toDAO(shippingInstructionTO.displayedNameForPlaceOfReceipt()))
        .displayedNameForPortOfLoad(
            displayedAddressMapper.toDAO(shippingInstructionTO.displayedNameForPortOfLoad()))
        .displayedNameForPlaceOfDelivery(
            displayedAddressMapper.toDAO(shippingInstructionTO.displayedNameForPlaceOfDelivery()))
        .displayedNameForPortOfDischarge(
            displayedAddressMapper.toDAO(shippingInstructionTO.displayedNameForPortOfDischarge()));
  }
}
