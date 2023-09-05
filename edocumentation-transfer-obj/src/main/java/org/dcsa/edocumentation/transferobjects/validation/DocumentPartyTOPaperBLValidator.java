package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;

public class DocumentPartyTOPaperBLValidator extends AbstractDocumentPartyTOValidator<DocumentPartyTOPaperBLValidation> {

  @Override
  public boolean isValid(DocumentPartyTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateEPIPartyCode(state, false);
    return state.isValid();
  }
}
