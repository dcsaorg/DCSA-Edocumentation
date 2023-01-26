package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.decoupled.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.decoupled.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.service.util.ShippingInstructionStateMachine;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;

  @Transactional(transactionManager = "decoupledTransactionManager")
  public ShippingInstructionTO findByReference(String shippingInstructionReference) {
    return shippingInstructionMapper.toDTO(getShippingInstructionOrThrow(shippingInstructionReference));
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public ShippingInstructionRefStatusTO createShippingInstruction(ShippingInstructionTO shippingInstructionTO) {
    OffsetDateTime now = OffsetDateTime.now();
    return saveShippingInstructionAndEvent(shippingInstructionTO, EblDocumentStatus.RECE, now, now, null);
  }

  @Transactional(transactionManager = "decoupledTransactionManager")
  public ShippingInstructionRefStatusTO updateShippingInstructionStatus(String shippingInstructionReference, EblDocumentStatus status, String reason) {
    return updateExistingShippingInstruction(shippingInstructionReference, shippingInstructionMapper::toDTO, status, reason);
  }

  private ShippingInstructionRefStatusTO updateExistingShippingInstruction(String shippingInstructionReference, Function<ShippingInstruction, ShippingInstructionTO> shippingInstructionSupplier, EblDocumentStatus documentStatus, String reason) {
    ShippingInstruction existingSI = getShippingInstructionOrThrow(shippingInstructionReference);
    ShippingInstructionTO shippingInstructionTO = shippingInstructionSupplier.apply(existingSI);
    ShippingInstructionStateMachine.validateTransition(existingSI.getDocumentStatus(), shippingInstructionMapper.toDAO(documentStatus), shippingInstructionTO.amendmentToTransportDocument() != null);

    OffsetDateTime now = OffsetDateTime.now();
    existingSI.lockVersion(now);
    shippingInstructionRepository.saveAndFlush(existingSI);

    return saveShippingInstructionAndEvent(shippingInstructionTO, documentStatus, existingSI.getCreatedDateTime(), now, reason);
  }

  private ShippingInstructionRefStatusTO saveShippingInstructionAndEvent(ShippingInstructionTO shippingInstructionTO, EblDocumentStatus documentStatus, OffsetDateTime created, OffsetDateTime updated, String reason) {
    ShippingInstructionTO updatedRequest = updateRequest(shippingInstructionTO, documentStatus, created, updated);
    ShippingInstruction shippingInstruction = shippingInstructionRepository.save(shippingInstructionMapper.toDAO(updatedRequest));
    shipmentEventRepository.save(shipmentEventFromSI(shippingInstruction, reason));
    return shippingInstructionMapper.toStatusDTO(updatedRequest);
  }

  private ShippingInstruction getShippingInstructionOrThrow(String shippingInstructionReference) {
    return shippingInstructionRepository.findByShippingInstructionReference(shippingInstructionReference)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No shipping instruction found with shippingInstructionReference: " + shippingInstructionReference));
  }

  private ShippingInstructionTO updateRequest(ShippingInstructionTO shippingInstructionTO, EblDocumentStatus documentStatus, OffsetDateTime createTime, OffsetDateTime updateTime) {
    String shippingInstructionReference =
      Objects.requireNonNullElseGet(shippingInstructionTO.shippingInstructionReference(), () -> UUID.randomUUID().toString());

    return shippingInstructionTO.toBuilder()
      .shippingInstructionReference(shippingInstructionReference)
      .shippingInstructionCreatedDateTime(createTime)
      .shippingInstructionUpdatedDateTime(updateTime)
      .documentStatus(documentStatus)
      .build();
  }

  private ShipmentEvent shipmentEventFromSI(ShippingInstruction shippingInstruction, String reason) {
    return ShipmentEvent.builder()
      .documentID(shippingInstruction.getId())
      .documentReference(shippingInstruction.getShippingInstructionReference())
      .documentTypeCode(DocumentTypeCode.SHI)
      .shipmentEventTypeCode(shippingInstruction.getDocumentStatus().asShipmentEventTypeCode())
      .reason(reason)
      .eventClassifierCode(EventClassifierCode.ACT)
      .eventCreatedDateTime(shippingInstruction.getUpdatedDateTime())
      .eventDateTime(shippingInstruction.getUpdatedDateTime())
      .build();
  }
}
