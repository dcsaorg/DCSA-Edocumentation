package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PartyIdentifyingCodeTO(
  @NotBlank @Size(max = 100)
  String partyCode,

  @NotNull
  @JsonProperty("DCSAResponsibleAgencyCode")
  DCSAResponsibleAgencyCode dcsaResponsibleAgencyCode,

  @Size(max = 100)
  String codeListName
) {
  @Builder(toBuilder = true)
  public PartyIdentifyingCodeTO { }
}
