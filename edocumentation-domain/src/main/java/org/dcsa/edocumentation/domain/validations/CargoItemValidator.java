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
    validateCustomsReferences(state,state.getValue().getCustomsReferences());
    return state.isValid();
  }

}
