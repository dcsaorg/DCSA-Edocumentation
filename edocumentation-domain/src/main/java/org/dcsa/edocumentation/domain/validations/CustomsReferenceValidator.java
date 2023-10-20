package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;

public class CustomsReferenceValidator implements ConstraintValidator<CustomsReferenceValidation, CustomsReference> {

  private static final CodeCache VALID_REFERENCE_CODES =
    CodeCache.of("customsreferences.csv", "Customs Reference Type Code", "Customs Reference Country Code");

  @Override
  public boolean isValid(CustomsReference value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateTypeAndCountryCodes(state);
    return state.isValid();
  }

  private void validateTypeAndCountryCodes(ValidationState<CustomsReference> state) {
    CustomsReference customsReference =  state.getValue();
    if (!VALID_REFERENCE_CODES.isValid(customsReference.getType(), customsReference.getCountryCode())) {
      state.getContext().buildConstraintViolationWithTemplate("Invalid combination of Type " + customsReference.getType() + " and Country Code "
          + customsReference.getCountryCode())
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
