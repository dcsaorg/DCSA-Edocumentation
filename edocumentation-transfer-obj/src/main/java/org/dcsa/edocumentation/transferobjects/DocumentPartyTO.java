package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.validation.DocumentPartyTOEBLValidation;
import org.dcsa.edocumentation.transferobjects.validation.DocumentPartyTOPaperBLValidation;
import org.dcsa.edocumentation.transferobjects.validation.EBLValidation;
import org.dcsa.edocumentation.transferobjects.validation.PaperBLValidation;

@DocumentPartyTOEBLValidation(groups = EBLValidation.class)
@DocumentPartyTOPaperBLValidation(groups = PaperBLValidation.class)
public record DocumentPartyTO(
  @NotNull
  @Valid PartyTO party,

  @NotNull
  // Async validated via @PseudoEnum
  String partyFunction,

  // TODO: Missing partyContactDetails here

  @Size(max = 5)
  List<@Size(max = 35) String> displayedAddress,

  @NotNull
  Boolean isToBeNotified
) {
  @Builder(toBuilder = true)
  public DocumentPartyTO { }
}
