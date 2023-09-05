package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;

public class DocumentPartyPaperBLValidator extends AbstractDocumentPartyValidator<DocumentPartyPaperBLValidation> {

  @Override
  public boolean isValid(DocumentParty value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateEPIPartyCode(state, false);
    return state.isValid();
  }
}
