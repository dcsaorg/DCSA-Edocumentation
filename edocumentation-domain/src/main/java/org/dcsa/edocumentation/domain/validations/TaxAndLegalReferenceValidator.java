package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.TaxAndLegalReference;

public class TaxAndLegalReferenceValidator implements ConstraintValidator<TaxAndLegalReferenceValidation, TaxAndLegalReference> {

  private static final CodeCache VALID_TAX_AND_LEGAL_CODES =
    CodeCache.of("taxandlegalreferences.csv", "Tax and Legal Reference Type", "Country Code");

  @Override
  public boolean isValid(TaxAndLegalReference value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    boolean valid =  VALID_TAX_AND_LEGAL_CODES.isValid(value.getType(),value.getCountryCode());
    if (!valid) {
      var state = ValidationState.of(value, context);
      state.getContext().buildConstraintViolationWithTemplate(
          "Illegal combination of Tax and Legal Reference Type = " + value.getType() + " and country code = " + value.getCountryCode())
        .addConstraintViolation();
      state.invalidate();
      return false;
    }
    return true;
  }
}
