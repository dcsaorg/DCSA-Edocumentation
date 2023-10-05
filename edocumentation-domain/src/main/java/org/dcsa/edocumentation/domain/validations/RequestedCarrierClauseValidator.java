package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class RequestedCarrierClauseValidator implements ConstraintValidator<RequestedCarrierClauseValidation, List<String>> {

  private static final CodeCache VALID_CLAUSE_CODES =
    CodeCache.of("carrierclauses.csv", "Carrier Clause Code");

  @Override
  public boolean isValid(List<String> value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateClauses(state);
    return state.isValid();
  }

  private void validateClauses(ValidationState<List<String>> state) {
    List<String> requestedClauses =  state.getValue();
    List<String> validatedClauses = requestedClauses.stream()
      .filter(item -> VALID_CLAUSE_CODES.isValid(item)).toList();

    if (requestedClauses.size() != validatedClauses.size()) {
      state.getContext().buildConstraintViolationWithTemplate("Provide the Valid Carrier Clauses " + requestedClauses)
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
