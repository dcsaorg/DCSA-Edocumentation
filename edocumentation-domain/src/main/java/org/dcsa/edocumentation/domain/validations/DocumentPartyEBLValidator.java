package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;

public class DocumentPartyEBLValidator extends AbstractDocumentPartyValidator<DocumentPartyEBLValidation> {

  @Override
  public boolean isValid(DocumentParty value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateEPIPartyCode(state, true);
    return state.isValid();
  }
}
