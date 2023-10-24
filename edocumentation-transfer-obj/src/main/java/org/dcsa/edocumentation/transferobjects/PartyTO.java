package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PartyTO(
  @Size(max = 100)
  String partyName,

  @Valid
  AddressTO address,

  List<@Valid TaxAndLegalReferenceTO> taxAndLegalReferences,

  @NotEmpty
  List<@Valid PartyContactDetailsTO> partyContactDetails,

  List<@Valid PartyIdentifyingCodeTO> identifyingCodes
) {
  @Builder(toBuilder = true)
  public PartyTO { }
}
