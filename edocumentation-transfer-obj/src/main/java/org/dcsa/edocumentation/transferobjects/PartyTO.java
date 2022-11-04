package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public record PartyTO(
  @Size(max = 100)
  String partyName,

  @Size(max = 20)
  String taxReference1,

  @Size(max = 20)
  String taxReference2,

  @Size(max = 500)
  String publicKey,

  @Valid
  AddressTO address,

  @Valid @NotEmpty
  List<PartyContactDetailsTO> partyContactDetails,

  @Valid
  List<PartyIdentifyingCodesTO> identifyingCodes
) {
  @Builder(toBuilder = true)
  public PartyTO { }
}
