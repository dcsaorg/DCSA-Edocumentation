package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.Location;

public class LocationValidator implements ConstraintValidator<LocationValidation, Location> {


  @Override
  public boolean isValid(Location value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    LocationSubType locationSubType = null;
    try {
      locationSubType = LocationSubType.valueOf(value.getLocationType());
    } catch (IllegalArgumentException e) {
      state.invalidate();
      context.buildConstraintViolationWithTemplate("Unknown location type")
        .addPropertyNode("locationType")
        .addConstraintViolation();
    }

    if (locationSubType != null) {
      locationSubType.checkFields(state);
    }

    return state.isValid();
  }
}
