package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;

import java.util.List;

public class ConsignmentItemValidator extends AbstractCustomsReferenceListValidator implements ConstraintValidator<ConsignmentItemValidation, ConsignmentItem> {

  @Override
  public boolean isValid(ConsignmentItem value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateConfirmedBookings(state);
    validateCustomsReference(state);
    return state.isValid();
  }

  private void validateConfirmedBookings(ValidationState<ConsignmentItem> state) {
    var shipment = state.getValue().getShipment();
    if (shipment.getBooking().getDocumentStatus() != BkgDocumentStatus.CONF) {
      state.getContext().buildConstraintViolationWithTemplate("The booking " + shipment.getCarrierBookingReference() + " is not in state CONF")
        // Match the TO path
        .addPropertyNode("carrierBookingReference")
        .addConstraintViolation();
      state.invalidate();
    }
  }

  private void validateCustomsReference(ValidationState<ConsignmentItem> state) {
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
