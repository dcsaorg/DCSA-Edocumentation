package org.dcsa.edocumentation.domain.validations;

import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;

@RequiredArgsConstructor(staticName = "of")
class ValidationState<T> {
  final T value;
  final ConstraintValidatorContext context;
  boolean isValid = true;

  public void applyResult(boolean validationPassed) {
    this.isValid = this.isValid && validationPassed;
  }

  public void invalidate() {
    this.isValid = false;
  }
}
