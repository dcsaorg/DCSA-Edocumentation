package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.AdvanceManifestFilingEBL;

public class AdvanceManifestFilingEBLValidator implements ConstraintValidator<AdvanceManifestFilingEBLValidation, AdvanceManifestFilingEBL> {

  private static final CodeCache VALID_MANIFEST_CODES =
    CodeCache.of("advancemanifestfilings.csv", "Advance Manifest Filing Type Code", "Country Code");

  @Override
  public boolean isValid(AdvanceManifestFilingEBL value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateManifestTypeAndCountryCodes(state);
    validateManifestFiling(state);
    return state.isValid();
  }

  private void validateManifestTypeAndCountryCodes(ValidationState<AdvanceManifestFilingEBL> state) {
    AdvanceManifestFilingEBL advanceManifestFilingEBL =  state.getValue();
    if (!VALID_MANIFEST_CODES.isValid(advanceManifestFilingEBL.getManifestTypeCode(),advanceManifestFilingEBL.getCountryCode())) {
      state.getContext().buildConstraintViolationWithTemplate("Mandatory to provide manifestTypeCode and countryCode for "
          + advanceManifestFilingEBL.getShippingInstruction().getShippingInstructionReference())
        .addConstraintViolation();
      state.invalidate();
    }
  }


  private void validateManifestFiling(ValidationState<AdvanceManifestFilingEBL> state) {
    AdvanceManifestFilingEBL advanceManifestFilingEBL =  state.getValue();
    if ( ("ACE".equals(advanceManifestFilingEBL.getManifestTypeCode())
      && "US".equals(advanceManifestFilingEBL.getCountryCode())) ||
      ("ACI".equals(advanceManifestFilingEBL.getManifestTypeCode())
        && "CA".equals(advanceManifestFilingEBL.getCountryCode())) ) {
      if ( "SHIPPER".equals(advanceManifestFilingEBL.getAdvanceManifestFilingsPerformedBy().name())) {
        if (advanceManifestFilingEBL.getSelfFilerCode() == null) {
          state.getContext().buildConstraintViolationWithTemplate("Mandatory to provide selfFilerCode for "
              + advanceManifestFilingEBL.getShippingInstruction().getShippingInstructionReference())
            // Match the TO path
            .addPropertyNode("selfFilerCode")
            .addConstraintViolation();
          state.invalidate();
        }
      }
    }
  }
}
