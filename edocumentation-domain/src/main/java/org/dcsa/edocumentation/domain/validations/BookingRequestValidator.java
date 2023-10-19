package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;

public class BookingRequestValidator implements ConstraintValidator<BookingRequestValidation, BookingRequest> {

  private static final Predicate<String> IS_SOURCE_LOCATION_TYPE = Set.of(LocationType.PRE.name(), LocationType.POL.name())::contains;
  private static final Predicate<String> IS_DESTINATION_LOCATION_TYPE = Set.of(LocationType.POD.name(), LocationType.PDE.name())::contains;
  private static final Predicate<String> IS_SOURCE_OR_DESTINATION_LOCATION_TYPE =
          IS_SOURCE_LOCATION_TYPE.or(IS_DESTINATION_LOCATION_TYPE);
  private static final Predicate<ShipmentLocation> IS_SOURCE_OR_DESTINATION_LOCATION =
          sl -> IS_SOURCE_OR_DESTINATION_LOCATION_TYPE.test(sl.getShipmentLocationTypeCode());
  private static final Predicate<ShipmentLocation> HAS_ADDRESS = sl -> sl.getLocation().getAddress() != null;
  private static final Predicate<ShipmentLocation> HAS_UNLOCATION_CODE = sl -> sl.getLocation().getUNLocationCode() != null;


  @Override
  public boolean isValid(BookingRequest value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateShipmentLocations(state);
    return state.isValid();
  }

  private void validateShipmentLocations(ValidationState<BookingRequest> state) {
    var shipmentLocations = state.getValue().getShipmentLocations();
    if (shipmentLocations == null || shipmentLocations.isEmpty()) {
      // This case is handled by a simple Jakarta annotation.
      return;
    }
    var context = state.getContext();

    boolean slHasPREorPOLs =
            shipmentLocations.stream()
                    .map(ShipmentLocation::getShipmentLocationTypeCode)
                    .anyMatch(IS_SOURCE_LOCATION_TYPE);

    if (!state.applyResult(slHasPREorPOLs)) {
      context.buildConstraintViolationWithTemplate(
              "No ShipmentLocationTypeCode of PRE or POL found in the shipmentLocations."
              + " At least one of them should be provided."
      ).addPropertyNode("shipmentLocations")
                      .addConstraintViolation();
    }

    boolean slHasPODorPDE =
            shipmentLocations.stream()
                    .map(ShipmentLocation::getShipmentLocationTypeCode)
                    .anyMatch(IS_DESTINATION_LOCATION_TYPE);

    if (!state.applyResult(slHasPODorPDE)) {
      context.buildConstraintViolationWithTemplate(
                      "No ShipmentLocationTypeCode of POD or PDE found in the shipmentLocations."
                              + " At least one of them should be provided."
              ).addPropertyNode("shipmentLocations")
              .addConstraintViolation();
    }


    var filteredByUNLocationCode =
            shipmentLocations.stream()
                    .filter(IS_SOURCE_OR_DESTINATION_LOCATION.and(HAS_UNLOCATION_CODE))
                    .toList();

    var filteredByUNLocationCodeCount = filteredByUNLocationCode.size();

    boolean hasUniqueUNLocationCodes =
            filteredByUNLocationCode.stream()
                    .map(sl -> sl.getLocation().getUNLocationCode())
                    .distinct()
                    .count()
                    == filteredByUNLocationCodeCount;

    if (!state.applyResult(hasUniqueUNLocationCodes)) {
      context.buildConstraintViolationWithTemplate(
                      "Duplicate UNLocationCodes found in shipmentLocations"
              ).addPropertyNode("shipmentLocations")
              .addConstraintViolation();
    }

    var filteredByAddress =
        shipmentLocations.stream()
            .filter(IS_SOURCE_OR_DESTINATION_LOCATION.and(HAS_ADDRESS))
            .map(sl -> sl.getLocation().getAddress())
            .filter(Objects::nonNull)
            // Clear the ID field to avoid false-negative matching due to entity IDs changing
            .map(a -> a.toBuilder().id(null).build())
            .toList();

    var filteredByAddressCount = filteredByAddress.size();

    boolean hasUniqueAddresses =
            filteredByAddress.stream()
                    // Relies on hashCode
                    .distinct()
                    .count()
                    == filteredByAddressCount;

    if (!state.applyResult(hasUniqueAddresses)) {
      context.buildConstraintViolationWithTemplate(
                      "Duplicate addresses found in shipmentLocations"
              ).addPropertyNode("shipmentLocations")
              .addConstraintViolation();
    }
  }
}
