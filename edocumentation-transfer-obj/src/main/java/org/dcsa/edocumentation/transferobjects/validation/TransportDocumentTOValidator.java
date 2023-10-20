package org.dcsa.edocumentation.transferobjects.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.PartyFunction;

public class TransportDocumentTOValidator implements ConstraintValidator<TransportDocumentTOValidation, TransportDocumentTO> {

  @Override
  public boolean isValid(TransportDocumentTO value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    context.disableDefaultConstraintViolation();

    var state = ValidationState.of(value, context);
    validateDates(state);
    validateShipper(state);
    validateQuotationReference(state);
    if (value.isToOrder() == Boolean.TRUE) {
      validateNegotiableBL(state);
    } else {
      validateStraightBL(state);
    }
    return state.isValid();
  }

  private void validateQuotationReference(ValidationState<TransportDocumentTO> state) {
    var td = state.getValue();
    if (td.contractQuotationReference() == null) {
      var hasSco = nullSafeStream(td.documentParties())
        .filter(p -> p.partyFunction()
        .equals(PartyFunction.SCO.name()))
        .count();
      if (state.applyResult(hasSco > 0)) {
        state.getContext()
          .buildConstraintViolationWithTemplate("Without a contractQuotationReference, a service contract owner (Party Function: SCO) must be present")
          .addConstraintViolation();
      }
    }
  }

  private void validateDates(ValidationState<TransportDocumentTO> state) {
    var td = state.getValue();
    var context = state.getContext();
    var issueDate = td.issueDate();
    if (issueDate == null && !state.applyResult(td.documentStatus().equals(EblDocumentStatus.DRAFT))) {
      context.buildConstraintViolationWithTemplate("The issueDate is mandatory for documents that have been issued (documentStatus != DRAFT)")
        .addPropertyNode("issueDate")
        .addConstraintViolation();
    }
    // TODO: Check issueDate is not in the future
    if (!state.applyResult(td.receivedForShipmentDate() != null ^ td.shippedOnBoardDate() != null)) {
      context.buildConstraintViolationWithTemplate("The transport document must have either receivedForShipmentDate or shippedOnBoardDate. However, they are mutually exclusive.")
        .addConstraintViolation();
      return;
    }
    if (issueDate == null) {
      return;
    }
    var compareDate = td.shippedOnBoardDate();
    var attrName = "shippedOnBoardDate";
    if (compareDate == null) {
      compareDate = td.receivedForShipmentDate();
      attrName = "receivedForShipmentDate";
    }
    assert compareDate != null;
    if (compareDate.isAfter(issueDate)) {
      context.buildConstraintViolationWithTemplate("The " + attrName + " must be before or equal to the issueDate on an issued document")
          .addPropertyNode(attrName)
          .addConstraintViolation();
      state.invalidate();
    }
  }

  private void validateStraightBL(ValidationState<TransportDocumentTO> state) {
    validateLimitOnPartyFunction(state, PartyFunction.END, 0);
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.CN.name(), PartyFunction.DDS.name()))) {
      validateAtMostOncePartyFunction(state, PartyFunction.CN);
      validateAtMostOncePartyFunction(state, PartyFunction.DDS);
      validateMutuallyExclusivePartyFunctions(state, Set.of(PartyFunction.CN.name(), PartyFunction.DDS.name()));
    }
  }


  private void validateAtMostOncePartyFunction(ValidationState<TransportDocumentTO> state, PartyFunction partyFunction) {
    validateLimitOnPartyFunction(state, partyFunction, 1);
  }

  private <T> Stream<T> nullSafeStream(Collection<T> c) {
    return c != null ? c.stream() : Stream.of();
  }

  private void validateMutuallyExclusivePartyFunctions(ValidationState<TransportDocumentTO> state, Set<String> partyFunctions) {
    var matches = nullSafeStream(state.getValue().documentParties())
      .map(DocumentPartyTO::partyFunction)
      .filter(partyFunctions::contains)
      .distinct()
      .toList();
    if (matches.size() < 2) {
      return;
    }
    String names = String.join(", ", matches);
    String message = "The following party functions cannot be used together: " + names;
    state.getContext().buildConstraintViolationWithTemplate(message)
      .addPropertyNode("documentParties")
      .addConstraintViolation();
    state.invalidate();
  }

  private void validateLimitOnPartyFunction(ValidationState<TransportDocumentTO> state, PartyFunction partyFunction, int limit) {
    var matches = nullSafeStream(state.getValue().documentParties())
      .filter(p -> p.partyFunction().equals(partyFunction.name()))
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

  private boolean validateAtLeastOneOfIsPresent(ValidationState<TransportDocumentTO> state,
                                                Set<String> partyFunctions) {
    var matches = nullSafeStream(state.getValue().documentParties())
      .filter(p -> partyFunctions.contains(p.partyFunction()))
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

  private void validateNegotiableBL(ValidationState<TransportDocumentTO> state) {
    validateAtMostOncePartyFunction(state, PartyFunction.END);

    validateLimitOnPartyFunction(state, PartyFunction.CN, 0);
    validateLimitOnPartyFunction(state, PartyFunction.DDS, 0);
  }


  private void validateShipper(ValidationState<TransportDocumentTO> state) {
    if (validateAtLeastOneOfIsPresent(state, Set.of(PartyFunction.OS.name(), PartyFunction.DDR.name()))) {
      validateAtMostOncePartyFunction(state, PartyFunction.OS);
      validateAtMostOncePartyFunction(state, PartyFunction.DDR);
      validateMutuallyExclusivePartyFunctions(state, Set.of(PartyFunction.OS.name(), PartyFunction.DDR.name()));
    }
  }
}
