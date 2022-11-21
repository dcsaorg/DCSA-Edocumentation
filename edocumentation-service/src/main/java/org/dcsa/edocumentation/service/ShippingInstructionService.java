package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {

  private final LocationService locationService;
  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;
  private final DocumentPartyService documentPartyService;
  private final ReferenceService referenceService;
  private final UtilizedTransportEquipmentService utilizedTransportEquipmentService;
  private final StuffingService stuffingService;

  @Transactional
  public Optional<ShippingInstructionTO> findByReference(String shippingInstructionReference) {
    // TODO: Verify the mapping (DDT-1296) and add positive postman tests with schema validation
    return shippingInstructionRepository
        .findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
        .map(shippingInstructionMapper::toDTO);
  }

  @Transactional
  public ShippingInstructionRefStatusTO createShippingInstruction(
      ShippingInstructionTO shippingInstructionTO) {
    // TODO: Verify the mapping (DDT-1296) and add positive + negative postman tests

    ShippingInstruction shippingInstruction = toDAOBuilder(shippingInstructionTO)
      .build();
    shipmentEventRepository.save(shippingInstruction.receive());

    shippingInstruction = shippingInstructionRepository.save(shippingInstruction);
    documentPartyService.createDocumentParties(shippingInstructionTO.documentParties(), shippingInstruction);
    referenceService.createReferences(shippingInstructionTO.references(), shippingInstruction);
    Map<String, UtilizedTransportEquipment> savedUtilizedTransportEquipments = utilizedTransportEquipmentService.createUtilizedTransportEquipment(shippingInstructionTO.utilizedTransportEquipments());
    stuffingService.createStuffing(shippingInstruction, savedUtilizedTransportEquipments, shippingInstructionTO.consignmentItems());
    return shippingInstructionMapper.toStatusDTO(shippingInstruction);
  }

  // Return a builder because PUT will need to copy in some extra fields.
  private ShippingInstruction.ShippingInstructionBuilder toDAOBuilder(
      ShippingInstructionTO shippingInstructionTO) {
    // TODO: Verify this stub (DDT-1296)
    return shippingInstructionMapper.toDAO(shippingInstructionTO).toBuilder()
      .placeOfIssue(locationService.ensureResolvable(shippingInstructionTO.placeOfIssue()));
  }
}
