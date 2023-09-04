package org.dcsa.edocumentation.domain.validations;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
class ValidationState<T> {
  private final T value;
  private final ConstraintValidatorContext context;
  private boolean isValid = true;

  public void applyResult(boolean validationPassed) {
    this.isValid = this.isValid && validationPassed;
  }

  public void invalidate() {
    this.isValid = false;
  }
}
