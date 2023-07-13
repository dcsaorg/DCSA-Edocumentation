package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.PartyFunction;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record DocumentPartyTO(
  @NotNull @Valid
  PartyTO party,

  @NotNull
  PartyFunction partyFunction,

  // TODO: Missing partyContactDetails here

  @Size(max = 5)
  List<@Size(max = 35) String> displayedAddress,

  @NotNull
  Boolean isToBeNotified
) {
  @Builder(toBuilder = true)
  public DocumentPartyTO { }
}
