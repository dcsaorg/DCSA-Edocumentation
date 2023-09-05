package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record PartyIdentifyingCodeTO(
  @NotBlank @Size(max = 100)
  String partyCode,

  @NotNull
  @Size(max = 5)
  @JsonProperty("DCSAResponsibleAgencyCode")
  String dcsaResponsibleAgencyCode,

  @Size(max = 100)
  String codeListName
) {
  @Builder(toBuilder = true)
  public PartyIdentifyingCodeTO { }
}
