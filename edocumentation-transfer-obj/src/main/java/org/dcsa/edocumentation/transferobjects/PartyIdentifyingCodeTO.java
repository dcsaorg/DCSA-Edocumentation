package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSAResponsibleAgencyCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PartyIdentifyingCodeTO(
  @NotBlank
  String partyCode,

  @NotNull
  DCSAResponsibleAgencyCode dcsaResponsibleAgencyCode,

  @Size(max = 100)
  String codeListName
) {
  @Builder(toBuilder = true)
  public PartyIdentifyingCodeTO { }
}
