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
    validateCustomsReferences(state,state.getValue().getCustomsReferences());
    return state.isValid();
  }

}
