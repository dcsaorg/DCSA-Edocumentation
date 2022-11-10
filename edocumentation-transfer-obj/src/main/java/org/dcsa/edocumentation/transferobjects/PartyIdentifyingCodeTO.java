package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PartyIdentifyingCodeTO(
  @NotBlank
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
