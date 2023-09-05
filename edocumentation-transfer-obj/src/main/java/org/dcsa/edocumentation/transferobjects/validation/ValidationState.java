package org.dcsa.edocumentation.transferobjects.validation;

import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class ValidationState<T> {
  private final T value;
  private final ConstraintValidatorContext context;
  private boolean isValid = true;

  public boolean applyResult(boolean validationPassed) {
    this.isValid = this.isValid && validationPassed;
    return validationPassed;
  }

  public void invalidate() {
    this.isValid = false;
  }
}
