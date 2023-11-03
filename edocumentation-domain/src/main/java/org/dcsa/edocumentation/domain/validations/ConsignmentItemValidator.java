package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.infra.enums.BookingStatus;


public class ConsignmentItemValidator extends AbstractCustomsReferenceListValidator implements ConstraintValidator<ConsignmentItemValidation, ConsignmentItem> {

  @Override
  public boolean isValid(ConsignmentItem value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateBookingAndCommodity(state);
    validateCustomsReferences(state,state.getValue().getCustomsReferences());
    return state.isValid();
  }

  private void validateBookingAndCommodity(ValidationState<ConsignmentItem> state) {
    var consignmentItem = state.getValue();
    var booking = consignmentItem.getBooking();
    if (booking == null) {
      state.getContext().buildConstraintViolationWithTemplate(
        "Could not resolve booking with reference "
          + consignmentItem.getCarrierBookingReference()
          + ": It is not a known carrier booking reference")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
      return;
    }
    if (!booking.getBookingStatus().equals(BookingStatus.CONFIRMED)) {
      state.getContext().buildConstraintViolationWithTemplate("The booking " + booking.getCarrierBookingReference() + " is not in state CONFIRMED")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
    }
    if (consignmentItem.getCommodity() == null) {
      state.getContext().buildConstraintViolationWithTemplate(
        "Could not resolve the commodity subreference "
          +  consignmentItem.getCommoditySubreference()
          + " on the booking with carrier booking reference "
          + consignmentItem.getCarrierBookingReference()
          + ": It is not a known commodity subreference on said booking")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
