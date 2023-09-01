package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;

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
    return state.isValid;
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
    var matches = nullSafeStream(state.value.getDocumentParties())
      .filter(p -> p.getPartyFunction().equals(partyFunction.name()))
      .count();
    if (matches > limit) {
      String messageTemplate = switch (limit) {
        case 0 -> "The party function " + partyFunction.name() + " cannot be used on this SI.";
        case 1 -> "There can only be at most one party with the partyFunction " + partyFunction.name();
        default -> "There can only be at most " + limit + " parties with the partyFunction " + partyFunction.name();
      };
      state.context.buildConstraintViolationWithTemplate(messageTemplate)
        .addPropertyNode("documentParties")
        .addConstraintViolation();
      state.invalidate();
      return false;
    }
    return true;
  }

  private boolean validateAtLeastOneOfIsPresent(ValidationState<ShippingInstruction> state,
                                                Set<String> partyFunctions) {
    var matches = nullSafeStream(state.value.getDocumentParties())
      .filter(p -> partyFunctions.contains(p.getPartyFunction()))
      .count();
    if (matches < 1) {
      state.context.buildConstraintViolationWithTemplate(
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

}
