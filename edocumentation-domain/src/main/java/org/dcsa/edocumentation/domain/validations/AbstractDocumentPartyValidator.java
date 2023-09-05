package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSAResponsibleAgencyCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;

public abstract class AbstractDocumentPartyValidator<A extends Annotation> implements ConstraintValidator<A, DocumentParty> {
  private static final Set<String> PFS_ON_THE_EBL_REQUIRING_PID = Set.of(
    PartyFunction.OS.name(),
    PartyFunction.CN.name(),
    PartyFunction.DDR.name(),
    PartyFunction.DDS.name(),
    PartyFunction.END.name()
  );

  protected void validateEPIPartyCode(ValidationState<DocumentParty> state, boolean isForEBL) {
    var documentParty = state.getValue();
    var partyFunction = documentParty.getPartyFunction();

    int epiCodes = (int)Objects.requireNonNullElseGet(documentParty.getParty().getIdentifyingCodes(), List::<PartyIdentifyingCode>of)
      .stream()
      .filter(c -> c.getDcsaResponsibleAgencyCode() == DCSAResponsibleAgencyCode.EPI)
      .count();
    final int expectedAmountOfEPICodes = isForEBL && PFS_ON_THE_EBL_REQUIRING_PID.contains(partyFunction) ? 1 : 0;


    if (epiCodes != expectedAmountOfEPICodes) {
      state.invalidate();
      String eBLType = isForEBL ? "an eBL" : "a paper B/L";
      var constraintMessage = switch (epiCodes) {
        case 0 -> "The document party with party function " + partyFunction + " must have an EPI party code when the B/L is " + eBLType + ".";
        case 1 -> "The document party with party function " + partyFunction + " must not have any EPI party codes when the B/L is " + eBLType + ".";
        default -> {
          if (expectedAmountOfEPICodes == 0) {
            String reason = PFS_ON_THE_EBL_REQUIRING_PID.contains(partyFunction)
              ? " when the B/L is " + eBLType + "."
              : ".";
            yield "The document party with party function "
              + partyFunction
              + " had multiple EPI party codes, but it cannot have any" + reason;
          }
          yield "The document party with party function "
            + partyFunction
            + " had multiple EPI party codes, but must have exactly one when the B/L is " + eBLType + ".";
        }
      };
      state.getContext().buildConstraintViolationWithTemplate(constraintMessage)
        .addPropertyNode("documentParties")
        .addConstraintViolation();
      if (epiCodes == 0) {
        state.getContext()
          .buildConstraintViolationWithTemplate("The document party with party function " + partyFunction + " had multiple EPI party codes, but it can have at most one.")
          .addPropertyNode("documentParties")
          .addConstraintViolation();
      }
    }
  }
}
