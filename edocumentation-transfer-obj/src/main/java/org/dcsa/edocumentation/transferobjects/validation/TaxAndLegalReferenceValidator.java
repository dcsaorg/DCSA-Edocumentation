package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.transferobjects.TaxAndLegalReferenceTO;

public class TaxAndLegalReferenceValidator implements ConstraintValidator<TaxAndLegalReferenceValidation, TaxAndLegalReferenceTO> {


  private static final CodeCache VALID_TAX_AND_LEGAL_CODES =
    CodeCache.of("taxandlegalreferences.csv", "Tax and Legal Reference Type", "Country Code");
  @Override
  public boolean isValid(TaxAndLegalReferenceTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    try {
      return VALID_TAX_AND_LEGAL_CODES.isValid(value.type(),value.countryCode());
    } catch (IllegalArgumentException e) {
    }
    return false;
  }
}
