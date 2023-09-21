package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.transferobjects.AdvanceManifestFilingTO;

public class AdvanceManifestFilingValidator implements ConstraintValidator<AdvanceManifestFilingValidation, AdvanceManifestFilingTO> {


  private static final CodeCache VALID_MANIFEST_CODES =
    CodeCache.of("advancedmanifestfilings.csv", "advance_manifest_filing_type_code", "country_code");
  @Override
  public boolean isValid(AdvanceManifestFilingTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    try {
      return VALID_MANIFEST_CODES.isValid(value.manifestTypeCode(),value.countryCode());
    } catch (IllegalArgumentException e) {
    }
    return false;
  }
}
