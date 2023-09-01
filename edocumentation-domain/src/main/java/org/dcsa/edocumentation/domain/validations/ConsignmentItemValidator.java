package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;

public class ConsignmentItemValidator implements ConstraintValidator<ConsignmentItemValidation, ConsignmentItem> {

  @Override
  public boolean isValid(ConsignmentItem value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateConfirmedBookings(state);
    return state.isValid;
  }

  private void validateConfirmedBookings(ValidationState<ConsignmentItem> state) {
    var shipment = state.value.getShipment();
    if (shipment.getBooking().getDocumentStatus() != BkgDocumentStatus.CONF) {
      state.context.buildConstraintViolationWithTemplate("The booking " + shipment.getCarrierBookingReference() + " is not in state CONF")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
