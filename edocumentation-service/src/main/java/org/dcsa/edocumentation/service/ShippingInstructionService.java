package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.DocumentPartyMapper;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {

  private final LocationService locationService;
  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;
  private final DocumentPartyService documentPartyService;
  private final DocumentPartyMapper documentPartyMapper;
  private final ReferenceService referenceService;
  private final UtilizedTransportEquipmentService utilizedTransportEquipmentService;

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

    OffsetDateTime now = OffsetDateTime.now();
    UUID shippingInstructionId = UUID.fromString("802ace2d-1f09-41e5-9176-020985455a6e");

    ShippingInstruction shippingInstruction =
        toDAOBuilder(shippingInstructionTO)
          .id(shippingInstructionId)
          .shippingInstructionReference("SI_REF_FOO")
          .shippingInstructionCreatedDateTime(now)
          .shippingInstructionUpdatedDateTime(now)
            .documentParties(
                shippingInstructionTO.documentParties().stream()
                    .map(documentPartyMapper::toDAO)
                    .collect(Collectors.toSet()))

            .build();
    //    shipmentEventRepository.save(shippingInstruction.receive());
    shippingInstruction.receive();

    shippingInstruction = shippingInstructionRepository.save(shippingInstruction);
    documentPartyService.createDocumentParties(shippingInstructionTO.documentParties(), shippingInstruction);
    referenceService.createReferences(shippingInstructionTO.references(), shippingInstruction);
    utilizedTransportEquipmentService.createUtilizedTransportEquipment(shippingInstructionTO.utilizedTransportEquipments());

    //    documentPartyService.createDocumentParties(shippingInstructionTO.documentParties());
    //    referenceService.createReferences(shippingInstructionTO.references(),
    // shippingInstruction);
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
