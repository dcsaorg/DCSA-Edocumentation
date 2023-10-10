package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;

import java.util.List;

public class UtilizedTransportEquipmentValidator extends AbstractCustomsReferenceListValidator implements ConstraintValidator<UtilizedTransportEquipmentValidation, UtilizedTransportEquipment> {

  @Override
  public boolean isValid(UtilizedTransportEquipment value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateCustomsReference(state);
    return state.isValid();
  }

  private void validateCustomsReference(ValidationState<UtilizedTransportEquipment> state) {
    if (state.getValue().getCustomsReferences() == null) {
      return;
    }
    List<CustomsReference> duplicateCustomsReference = checkReferencesDuplicates(state.getValue().getCustomsReferences());
    if (duplicateCustomsReference.size() >1 ) {
      state.getContext().buildConstraintViolationWithTemplate("The customsreferences contains duplicate combination of Type code and Country code." )
        // Match the TO path
        .addConstraintViolation();
      state.invalidate();
    }
  }

}
