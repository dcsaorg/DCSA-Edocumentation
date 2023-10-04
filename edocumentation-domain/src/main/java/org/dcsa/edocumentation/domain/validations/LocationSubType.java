package org.dcsa.edocumentation.domain.validations;

import org.dcsa.edocumentation.domain.persistence.entity.Location;

import java.util.Map;
import java.util.function.Function;

public enum LocationSubType {
  ADDR,
  UNLO,
  FACI,
  ;

  private static final Map<String, CodeCache> FACILITY_CODES = Map.of(
    "SMDG", CodeCache.of("facilities.csv", "UNLOCODE", "Facility SMDG Code"),
    "BIC", CodeCache.of("facilities.csv", "UNLOCODE", "Facility BIC Code")
  );
  private static final CodeCache UN_LOCATION_CODES = CodeCache.of("unlocationcodes.csv");
  private static final String ADDRESS_FIELD_NAME = "address";
  private static final String UN_LOC_CODE_FIELD_NAME = "UNLocationCode";
  private static final String FACILITY_CODE_FIELD_NAME = "facilityCode";
  private static final String FACILITY_CODE_LIST_PROVIDER_FIELD_NAME = "facilityCodeListProvider";

  void checkFields(ValidationState<Location> state) {
    checkAddress(state);
    checkUNLocationCodeAndFacility(state);
  }

  private void checkUNLocationCodeAndFacility(ValidationState<Location> state) {
    var location = state.getValue();
    var context = state.getContext();
    var ok =
      checkFieldRequiredOrOmitted(
        this != ADDR,
        Location::getUNLocationCode,
        state,
        UN_LOC_CODE_FIELD_NAME);
    var unLocationCode = location.getUNLocationCode();
    if (ok && this != ADDR && !UN_LOCATION_CODES.isValid(unLocationCode)) {
      ok = false;
      state.invalidate();
      context
        .buildConstraintViolationWithTemplate(
          "Unknown or invalid code "
            + " (Note: The reference implementation may not have a full dataset and this error may be a false positive)")
        .addPropertyNode(UN_LOC_CODE_FIELD_NAME)
        .addConstraintViolation();
    }
    if (!checkFieldRequiredOrOmitted(
      this == FACI,
      Location::getFacilityCode,
      state,
      FACILITY_CODE_FIELD_NAME)) {
      ok = false;
    }
    if (!checkFieldRequiredOrOmitted(
      this == FACI,
      Location::getFacilityCodeListProvider,
      state,
      FACILITY_CODE_LIST_PROVIDER_FIELD_NAME)) {
      ok = false;
    }
    if (this == FACI && ok) {
      assert unLocationCode != null;
      var codeCache = FACILITY_CODES.get(location.getFacilityCodeListProvider());
      if (codeCache == null) {
        state.invalidate();
        context
          .buildConstraintViolationWithTemplate(
            "Must be one of: " + String.join(", ", FACILITY_CODES.keySet()))
          .addPropertyNode(FACILITY_CODE_LIST_PROVIDER_FIELD_NAME)
          .addConstraintViolation();
        return;
      }
      if (!codeCache.isValid(unLocationCode, location.getFacilityCode())) {
        state.invalidate();
        context
          .buildConstraintViolationWithTemplate(
            "Unknown or invalid code "
              + " (Note: The reference implementation may not have a full dataset and this error may be a false positive)")
          .addPropertyNode(FACILITY_CODE_FIELD_NAME)
          .addConstraintViolation();
      }
    }
  }

  private void checkAddress(ValidationState<Location> state) {
    checkFieldRequiredOrOmitted(
      this == ADDR, Location::getAddress, state, ADDRESS_FIELD_NAME);
  }

  private boolean checkFieldRequiredOrOmitted(
    boolean isRequired,
    Function<Location, Object> getter,
    ValidationState<Location> state,
    String fieldName) {
    if (isRequired) {
      return isRequired(getter, state, fieldName);
    }
    return mustBeAbsent(getter, state, fieldName);
  }

  private boolean isRequired(
    Function<Location, Object> getter, ValidationState<Location> state, String fieldName) {
    if (getter.apply(state.getValue()) != null) {
      return true;
    }
    state.invalidate();
    state
      .getContext()
      .buildConstraintViolationWithTemplate(
        "The field " + fieldName + " is required for locationType " + this.name())
      .addPropertyNode(fieldName)
      .addConstraintViolation();
    return false;
  }

  private boolean mustBeAbsent(
    Function<Location, Object> getter, ValidationState<Location> state, String fieldName) {
    if (getter.apply(state.getValue()) == null) {
      return true;
    }
    state.invalidate();
    state
      .getContext()
      .buildConstraintViolationWithTemplate(
        "The field " + fieldName + " must be absent for locationType " + this.name())
      .addPropertyNode(fieldName)
      .addConstraintViolation();
    return false;
  }


}
