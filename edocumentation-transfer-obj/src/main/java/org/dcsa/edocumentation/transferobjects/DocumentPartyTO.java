package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.PartyFunction;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record DocumentPartyTO(
  @NotNull @Valid
  PartyTO party,

  @NotNull
  PartyFunction partyFunction,

  @Size(max = 6)
  List<@Size(max = 35) String> displayedAddress,

  @NotNull
  Boolean isToBeNotified
) {
  @Builder(toBuilder = true)
  public DocumentPartyTO { }
}
