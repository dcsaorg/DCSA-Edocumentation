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
    boolean validChecks = VALID_MANIFEST_CODES.isValid(value.getManifestTypeCode(),value.getCountryCode());
    var state = ValidationState.of(value, context);
    validateManifestFiling(state);
    return state.isValid() && validChecks ;
  }

  private void validateManifestFiling(ValidationState<AdvanceManifestFilingEBL> state) {
    AdvanceManifestFilingEBL advanceManifestFilingEBL =  state.getValue();
    if ( ("ACE".equalsIgnoreCase(advanceManifestFilingEBL.getManifestTypeCode())
      && "US".equalsIgnoreCase(advanceManifestFilingEBL.getCountryCode())) ||
      ("ACI".equalsIgnoreCase(advanceManifestFilingEBL.getManifestTypeCode())
        && "CA".equalsIgnoreCase(advanceManifestFilingEBL.getCountryCode())) ) {
      if ( "SHIPPER".equalsIgnoreCase(advanceManifestFilingEBL.getAdvanceManifestFilingsPerformedBy())) {
        if (advanceManifestFilingEBL.getSelfFilerCode() == null) {
          state.getContext().buildConstraintViolationWithTemplate("Mandatory to provide selfFilerCode for "
              + advanceManifestFilingEBL.getShippingInstruction().getShippingInstructionReference() + " is ")
            // Match the TO path
            .addPropertyNode("advanceManifestFilings")
            .addConstraintViolation();
          state.invalidate();
        }
      }
    }
  }
}
