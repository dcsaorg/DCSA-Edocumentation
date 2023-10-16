package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.infra.enums.BookingStatus;

public class ConsignmentItemValidator implements ConstraintValidator<ConsignmentItemValidation, ConsignmentItem> {

  @Override
  public boolean isValid(ConsignmentItem value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateConfirmedBookings(state);
    return state.isValid();
  }

  private void validateConfirmedBookings(ValidationState<ConsignmentItem> state) {
    var shipment = state.getValue().getShipment();
    if (!shipment.getBooking().getBookingStatus().equals(BookingStatus.CONFIRMED)) {
      state.getContext().buildConstraintViolationWithTemplate("The booking " + shipment.getCarrierBookingReference() + " is not in state CONFIRMED")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
