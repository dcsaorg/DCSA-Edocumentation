package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.*;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialShippingInstructionService {

  private final ShippingInstructionMapper shippingInstructionMapper;
  private final ShippingInstructionRepository shippingInstructionRepository;
  @Qualifier("eagerValidator")
  private final Validator validator;

  @Transactional
  public Optional<ShippingInstructionRefStatusTO> changeState(
    String shippingInstructionReference,
    String documentStatus) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    if (documentStatus.equals(EblDocumentStatus.PENDING_UPDATE)) {
      shippingInstruction.pendingUpdate();
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput("Cannot go to state " + documentStatus);
    }
    // Note this only works for cases where we can update the documentStatus in-place.
    shippingInstruction = shippingInstructionRepository.save(shippingInstruction);
    return Optional.of(shippingInstructionMapper.toStatusDTO(shippingInstruction));
  }

  public Optional<ShippingInstructionRefStatusTO> validateShippingInstruction(String shippingInstructionReference) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    performValidationOfShippingInstruction(shippingInstruction);
    return Optional.of(shippingInstructionMapper.toStatusDTO(shippingInstruction));
  }

  boolean performValidationOfShippingInstruction(ShippingInstruction shippingInstruction) {
    ValidationResult<String> validationResult;
    try {
      validationResult = shippingInstruction.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict("SI is not in a state to be DRAFT'ed", e);
    }
    if (!validationResult.validationErrors().isEmpty()) {
      var documentStatus = validationResult.proposedStatus();
      var reason = validationResult.presentErrors(5000);
      if (documentStatus.equals(EblDocumentStatus.REJECTED)) {
        shippingInstruction.rejected(reason);
      } else {
        assert documentStatus.equals(EblDocumentStatus.PENDING_UPDATE);
        shippingInstruction.pendingUpdate();
      }
      shippingInstructionRepository.save(shippingInstruction);
      return false;
    }
    return true;
  }

}
