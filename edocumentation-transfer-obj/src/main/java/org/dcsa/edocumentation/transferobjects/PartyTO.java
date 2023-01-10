package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.skernel.infrastructure.transferobject.AddressTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
  List<PartyIdentifyingCodeTO> identifyingCodes
) {
  @Builder(toBuilder = true)
  public PartyTO { }
}
