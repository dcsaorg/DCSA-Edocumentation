package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialShippingInstructionService {

  private final ShipmentEventRepository shipmentEventRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final DocumentStatusMapper documentStatusMapper;

  @Transactional
  public Optional<ShippingInstructionRefStatusTO> changeState(
    String shippingInstructionReference,
    org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus status,
    String reason) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    ShipmentEvent event = switch (documentStatusMapper.toDomainEblDocumentStatus(status)) {
      case PENU -> shippingInstruction.pendingUpdate(reason);
      default -> throw ConcreteRequestErrorMessageException.invalidInput("Cannot go to state " + status);
    };

    shipmentEventRepository.save(event);
    // Note this only works for cases where we can update the documentStatus in-place.
    shippingInstruction = shippingInstructionRepository.save(shippingInstruction);
    return Optional.of(shippingInstructionMapper.toStatusDTO(shippingInstruction));
  }
}
