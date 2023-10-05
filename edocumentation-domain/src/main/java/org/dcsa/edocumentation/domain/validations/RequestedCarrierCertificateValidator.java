package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class RequestedCarrierCertificateValidator implements ConstraintValidator<RequestedCarrierCertificateValidation, List<String>> {

  private static final CodeCache VALID_CERTIFICATE_CODES =
    CodeCache.of("carriercertificates.csv", "Carrier Certificate Code");

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateCertificates(state);
    return state.isValid();
  }

  private void validateCertificates(ValidationState<List<String>> state) {
    List<String> requestedCertificates =  state.getValue();
    List<String> validatedCertificates = requestedCertificates.stream()
      .filter(item -> VALID_CERTIFICATE_CODES.isValid(item)).toList();

    if (requestedCertificates.size() != validatedCertificates.size()) {
      state.getContext().buildConstraintViolationWithTemplate("Provide the Valid Carrier Certificates " + requestedCertificates)
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
