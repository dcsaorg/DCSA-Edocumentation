package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CargoMovementType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReceiptDeliveryType;

public class ShippingInstructionValidator extends AbstractCustomsReferenceListValidator implements ConstraintValidator<ShippingInstructionValidation, ShippingInstruction> {

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
      var booking = item.getBooking();
      if (booking == null) {
        continue;
      }
      var bookingData = booking.getLastConfirmedBookingData();
      if (bookingData == null) {
        continue;
      }
      receiptTypeAtOriginChecker.check(bookingData.getReceiptTypeAtOrigin());
      deliveryTypeAtDestinationChecker.check(bookingData.getDeliveryTypeAtDestination());
      cargoMovementTypeAtOriginChecker.check(bookingData.getCargoMovementTypeAtOrigin());
      cargoMovementTypeAtDestinationChecker.check(bookingData.getCargoMovementTypeAtDestination());
      termAndConditionsChecker.check(bookingData.getTermsAndConditions());
      serviceContractReferenceChecker.check(bookingData.getServiceContractReference());
    }

    emitConsignmentItemsConstraintIfNotOk(receiptTypeAtOriginChecker, state, "All referenced bookings must have the same receiptTypeAtOrigin");
    emitConsignmentItemsConstraintIfNotOk(deliveryTypeAtDestinationChecker, state, "All referenced bookings must have the same deliveryTypeAtDestination");
    emitConsignmentItemsConstraintIfNotOk(cargoMovementTypeAtOriginChecker, state, "All referenced bookings must have the same cargoMovementTypeAtOrigin");
    emitConsignmentItemsConstraintIfNotOk(cargoMovementTypeAtDestinationChecker, state, "All referenced bookings must have the same cargoMovementTypeAtDestination");
    emitConsignmentItemsConstraintIfNotOk(termAndConditionsChecker, state, "All referenced bookings must have the same termsAndConditions");
    emitConsignmentItemsConstraintIfNotOk(serviceContractReferenceChecker, state, "All referenced bookings must have the same serviceContractReference");
    validateManifestFilings(state);
    validateCustomsReferences(state,state.getValue().getCustomsReferences());
  }

  private void validateStraightBL(ValidationState<ShippingInstruction> state) {
    validateLimitOnPartyFunction(state, PartyFunction.END, 0);
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.CN.name(), PartyFunction.DDS.name()))) {
      validateAtMostOncePartyFunction(state, PartyFunction.CN);
      validateAtMostOncePartyFunction(state, PartyFunction.DDS);
    }
  }

  private void validateAtMostOncePartyFunction(ValidationState<ShippingInstruction> state, PartyFunction partyFunction) {
    validateLimitOnPartyFunction(state, partyFunction, 1);
  }

  private <T> Stream<T> nullSafeStream(Collection<T> c) {
    return c != null ? c.stream() : Stream.of();
  }

  private void validateLimitOnPartyFunction(ValidationState<ShippingInstruction> state, PartyFunction partyFunction, int limit) {
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
    }
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

    validateLimitOnPartyFunction(state, PartyFunction.CN, 0);
    validateLimitOnPartyFunction(state, PartyFunction.DDS, 0);
  }


  private void validateShipper(ValidationState<ShippingInstruction> state) {
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.OS.name(), PartyFunction.DDR.name()))) {
      validateAtMostOncePartyFunction(state, PartyFunction.OS);
      validateAtMostOncePartyFunction(state, PartyFunction.DDR);
    }
  }

  private void validateManifestFilings(ValidationState<ShippingInstruction> state) {
    var si = state.getValue();

    List<AdvanceManifestFilingEBL> advanceManifestFilingSIs = si.getAdvanceManifestFilings();
    List<ConsignmentItem> consignmentItems = si.getConsignmentItems();

    var firstBooking = si.getConsignmentItems().stream()
      .map(ConsignmentItem::getBooking)
      .filter(Objects::nonNull)
      .findFirst()
      .orElse(null);

    if (firstBooking == null) {
      // ConsignmentItemValidator flags Booking being null, so we are silent here.
      return;
    }
    var bookingData = firstBooking.getLastConfirmedBookingData();
    if (bookingData == null) {
      // ConsignmentItemValidator flags booking having the wrong status, so we are silent here.
      return;
    }

    List<AdvanceManifestFiling> advanceManifestFilingBase = bookingData.getAdvanceManifestFilings();
    Map<String,List<AdvanceManifestFiling>> misMatchedConsignmentManifestFilings = new HashMap<>();

    for (ConsignmentItem ci : consignmentItems) {
      var confirmedBooking = ci.getBooking();
      if (confirmedBooking == null) {
        // ConsignmentItemValidator flags shipment being null, so we are silent here.
        continue;
      }
      List<AdvanceManifestFiling> advanceManifestFilings = bookingData.getAdvanceManifestFilings();
      List<AdvanceManifestFiling> misMatchedManifestFilings = advanceManifestFilingBase.stream()
        .filter(two -> advanceManifestFilings.stream()
          .noneMatch(one -> one.getManifestTypeCode().equals(two.getManifestTypeCode())
            && one.getCountryCode().equals(two.getCountryCode())))
        .toList();
      if (!misMatchedManifestFilings.isEmpty()) {
        misMatchedConsignmentManifestFilings.put(ci.getBooking().getCarrierBookingReference(),misMatchedManifestFilings);
      }
    }

    if (!misMatchedConsignmentManifestFilings.isEmpty()) {
      List<String> carrierBookingRefs = misMatchedConsignmentManifestFilings.keySet().stream().toList();
      state.getContext().buildConstraintViolationWithTemplate(
        "Mismatch advance Manifest filings in carrier Booking References " + carrierBookingRefs)
        .addPropertyNode("advanceManifestFilings")
        .addConstraintViolation();
      state.invalidate();
      return;
    }

    List<AdvanceManifestFiling> misMatchedManifestFilingsSI =  advanceManifestFilingBase.stream()
      .filter(two -> advanceManifestFilingSIs.stream()
        .noneMatch(one -> one.getManifestTypeCode().equals(two.getManifestTypeCode())
          && one.getCountryCode().equals(two.getCountryCode())))
      .toList();

    if (!misMatchedManifestFilingsSI.isEmpty()) {
      state.getContext().buildConstraintViolationWithTemplate(
        "The Advance Manifest Filings in Shipping Instruction " + state.getValue().getShippingInstructionReference() + " doesn't match with the Shipments"
      ).addPropertyNode("advanceManifestFilings")
      .addConstraintViolation();
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
