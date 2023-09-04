package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CargoMovementType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReceiptDeliveryType;

public class ShippingInstructionValidator implements ConstraintValidator<ShippingInstructionValidation, ShippingInstruction> {

  @Override
  public boolean isValid(ShippingInstruction value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateShipper(state);
    if (value.getIsToOrder() == Boolean.TRUE) {
      validateNegotiableBL(state);
    } else {
      validateStraightBL(state);
    }
    validateShipmentAlignment(state);
    return state.isValid();
  }

  private void validateShipmentAlignment(ValidationState<ShippingInstruction> state) {
    var receiptTypeAtOriginChecker = EnsureEqual.<ReceiptDeliveryType>checker();
    var deliveryTypeAtDestinationChecker = EnsureEqual.<ReceiptDeliveryType>checker();
    var cargoMovementTypeAtOriginChecker = EnsureEqual.<CargoMovementType>checker();
    var cargoMovementTypeAtDestinationChecker = EnsureEqual.<CargoMovementType>checker();
    var termAndConditionsChecker = EnsureEqual.<String>checker();
    var serviceContractReferenceChecker = EnsureEqual.<String>checker();

    /*
      There is a list of fields that must be the same if multiple bookings are being linked to from the same SI:

      transportPlan -- FIXME: Not implemented yet
      shipmentLocations -- FIXME: Not implemented yet
      receiptTypeAtOrigin
      deliveryTypeAtDestination
      cargoMovementTypeAtOrigin
      cargoMovementTypeAtDestination
      serviceContractReference
      termsAndConditions
      Place of B/L Issuance (if provided) -- FIXME: Not implemented yet

      This means that we can just pick *any* of the bookings/shipments when resolving these fields for the
      purpose of figuring out the value.

      Note: If the SI has its own copy of the field, it overrules the shipments/bookings and then we do not
      require alignment when the SI version is not null.  This is relevant for Place of B/L Issuance.
   */

    for (var item : state.getValue().getConsignmentItems()) {
      var shipment = item.getShipment();
      if (shipment == null) {
        continue;
      }
      var booking = shipment.getBooking();
      receiptTypeAtOriginChecker.check(booking.getReceiptTypeAtOrigin());
      deliveryTypeAtDestinationChecker.check(booking.getDeliveryTypeAtDestination());
      cargoMovementTypeAtOriginChecker.check(booking.getCargoMovementTypeAtOrigin());
      cargoMovementTypeAtDestinationChecker.check(booking.getCargoMovementTypeAtDestination());
      termAndConditionsChecker.check(shipment.getTermsAndConditions());
      serviceContractReferenceChecker.check(booking.getServiceContractReference());
    }

    emitConsignmentItemsConstraintIfNotOk(receiptTypeAtOriginChecker, state, "All referenced bookings must have the same receiptTypeAtOrigin");
    emitConsignmentItemsConstraintIfNotOk(deliveryTypeAtDestinationChecker, state, "All referenced bookings must have the same deliveryTypeAtDestination");
    emitConsignmentItemsConstraintIfNotOk(cargoMovementTypeAtOriginChecker, state, "All referenced bookings must have the same cargoMovementTypeAtOrigin");
    emitConsignmentItemsConstraintIfNotOk(cargoMovementTypeAtDestinationChecker, state, "All referenced bookings must have the same cargoMovementTypeAtDestination");
    emitConsignmentItemsConstraintIfNotOk(termAndConditionsChecker, state, "All referenced bookings must have the same termsAndConditions");
    emitConsignmentItemsConstraintIfNotOk(serviceContractReferenceChecker, state, "All referenced bookings must have the same serviceContractReference");
  }

  private void validateStraightBL(ValidationState<ShippingInstruction> state) {
    state.applyResult(validateLimitOnPartyFunction(state, PartyFunction.END, 0));
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.CN.name(), PartyFunction.DDS.name()))) {
      state.applyResult(validateAtMostOncePartyFunction(state, PartyFunction.CN));
      state.applyResult(validateAtMostOncePartyFunction(state, PartyFunction.DDS));
    } else {
      state.invalidate();
    }
  }


  private boolean validateAtMostOncePartyFunction(ValidationState<ShippingInstruction> state, PartyFunction partyFunction) {
    return validateLimitOnPartyFunction(state, partyFunction, 1);
  }

  private <T> Stream<T> nullSafeStream(Collection<T> c) {
    return c != null ? c.stream() : Stream.of();
  }

  private boolean validateLimitOnPartyFunction(ValidationState<ShippingInstruction> state, PartyFunction partyFunction, int limit) {
    var matches = nullSafeStream(state.getValue().getDocumentParties())
      .filter(p -> p.getPartyFunction().equals(partyFunction.name()))
      .count();
    if (matches > limit) {
      String messageTemplate = switch (limit) {
        case 0 -> "The party function " + partyFunction.name() + " cannot be used on this SI.";
        case 1 -> "There can only be at most one party with the partyFunction " + partyFunction.name();
        default -> "There can only be at most " + limit + " parties with the partyFunction " + partyFunction.name();
      };
      state.getContext().buildConstraintViolationWithTemplate(messageTemplate)
        .addPropertyNode("documentParties")
        .addConstraintViolation();
      state.invalidate();
      return false;
    }
    return true;
  }

  private boolean validateAtLeastOneOfIsPresent(ValidationState<ShippingInstruction> state,
                                                Set<String> partyFunctions) {
    var matches = nullSafeStream(state.getValue().getDocumentParties())
      .filter(p -> partyFunctions.contains(p.getPartyFunction()))
      .count();
    if (matches < 1) {
      state.getContext().buildConstraintViolationWithTemplate(
        "This SI requires at least one of the following party functions to be present: " +
          String.join(", ", partyFunctions.stream().toList())
      ).addPropertyNode("documentParties")
        .addConstraintViolation();
      state.invalidate();
      return false;
    }
    return true;
  }

  private void validateNegotiableBL(ValidationState<ShippingInstruction> state) {
    validateAtMostOncePartyFunction(state, PartyFunction.END);

    validateLimitOnPartyFunction(state, PartyFunction.OS, 0);
    validateLimitOnPartyFunction(state, PartyFunction.DDR, 0);
  }


  private void validateShipper(ValidationState<ShippingInstruction> state) {
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.OS.name(), PartyFunction.DDR.name()))) {
      state.applyResult(validateAtMostOncePartyFunction(state, PartyFunction.OS));
      state.applyResult(validateAtMostOncePartyFunction(state, PartyFunction.DDR));
    } else {
      state.invalidate();
    }
  }

  private static void emitConsignmentItemsConstraintIfNotOk(
    EnsureEqual<?> checker,
    ValidationState<ShippingInstruction> state,
    String message
  ) {
    if (checker.isValid) {
      return;
    }
    state.getContext().buildConstraintViolationWithTemplate(message)
      .addPropertyNode("consignmentItems")
      .addConstraintViolation();
  }

  @RequiredArgsConstructor
  private static class EnsureEqual<T> {

    private final BiPredicate<T, T> equalityChecker;

    private boolean isSet = false;
    private T value = null;
    private boolean isValid = true;

    void check(T newValue) {
      if (newValue == null) {
        return;
      }
      if (!isSet) {
        value = newValue;
        isSet = true;
      } else if (isValid) {
        isValid = equalityChecker.test(this.value, newValue);
      }
    }
    static <T> EnsureEqual<T> checker() {
      return checker(Objects::equals);
    }
    static <T> EnsureEqual<T> checker(BiPredicate<T, T> equalityChecker) {
      return new EnsureEqual<>(equalityChecker);
    }
  }

}
