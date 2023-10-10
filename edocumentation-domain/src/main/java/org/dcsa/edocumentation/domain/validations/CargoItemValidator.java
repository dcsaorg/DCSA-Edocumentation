package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;

import java.util.List;

public class CargoItemValidator extends AbstractCustomsReferenceListValidator implements ConstraintValidator<CargoItemValidation, CargoItem> {

  @Override
  public boolean isValid(CargoItem value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();
    var state = ValidationState.of(value, context);
    validateCustomsReference(state);
    return state.isValid();
  }

  private void validateCustomsReference(ValidationState<CargoItem> state) {
    if (state.getValue().getCustomsReferences() == null) {
      return;
    }
    List<CustomsReference> duplicateCustomsReference = checkReferencesDuplicates(state.getValue().getCustomsReferences());
    if (duplicateCustomsReference.size() >1 ) {
      state.getContext().buildConstraintViolationWithTemplate("The customsreferences contains duplicate combination of Type code and Country code." )
        // Match the TO path
        .addPropertyNode("customsReferences")
        .addConstraintViolation();
      state.invalidate();
    }
  }

}
