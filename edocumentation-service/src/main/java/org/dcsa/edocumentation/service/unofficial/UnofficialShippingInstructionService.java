package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.*;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
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
  private final DocumentStatusMapper documentStatusMapper;
  @Qualifier("eagerValidator")
  private final Validator validator;

  @Transactional
  public Optional<ShippingInstructionRefStatusTO> changeState(
    String shippingInstructionReference,
    org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus status) {
    ShippingInstruction shippingInstruction = shippingInstructionRepository.findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
      .orElse(null);
    if (shippingInstruction == null) {
      return Optional.empty();
    }
    if (Objects.requireNonNull(documentStatusMapper.toDomainEblDocumentStatus(status)) == EblDocumentStatus.PENU) {
      shippingInstruction.pendingUpdate();
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput("Cannot go to state " + status);
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
    ValidationResult<EblDocumentStatus> validationResult;
    try {
      validationResult = shippingInstruction.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict("SI is not in a state to be DRFT'ed", e);
    }
    if (!validationResult.validationErrors().isEmpty()) {
      var status = validationResult.proposedStatus();
      var reason = validationResult.presentErrors(5000);
      if (status == EblDocumentStatus.REJE) {
        shippingInstruction.rejected(reason);
      } else {
        assert status == EblDocumentStatus.PENU;
        shippingInstruction.pendingUpdate();
      }
      shippingInstructionRepository.save(shippingInstruction);
      return false;
    }
    return true;
  }

}
