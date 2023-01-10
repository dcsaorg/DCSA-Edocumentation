package org.dcsa.edocumentation.transferobjects.validation;

import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class UtilizedTransportEquipmentValidator
    implements ConstraintValidator<ValidUtilizedTransportEquipment, UtilizedTransportEquipmentTO> {

  @Override
  public void initialize(ValidUtilizedTransportEquipment constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(
      UtilizedTransportEquipmentTO utilizedTransportEquipmentTO,
      ConstraintValidatorContext constraintValidatorContext) {
    if (utilizedTransportEquipmentTO != null) {
      if (utilizedTransportEquipmentTO.isShipperOwned()) {
        return containsWeightAndUnit(utilizedTransportEquipmentTO.equipment());
      }
      return true;
    }
    return false;
  }

  private boolean containsWeightAndUnit(EquipmentTO equipment) {
    return Objects.nonNull(equipment.tareWeight()) && Objects.nonNull(equipment.weightUnit());
  }
}
