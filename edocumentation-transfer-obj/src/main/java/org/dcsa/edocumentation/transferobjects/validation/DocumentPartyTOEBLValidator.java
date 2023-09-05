package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;

public class DocumentPartyTOEBLValidator extends AbstractDocumentPartyTOValidator<DocumentPartyTOEBLValidation> {

  @Override
  public boolean isValid(DocumentPartyTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateEPIPartyCode(state, true);
    return state.isValid();
  }
}
