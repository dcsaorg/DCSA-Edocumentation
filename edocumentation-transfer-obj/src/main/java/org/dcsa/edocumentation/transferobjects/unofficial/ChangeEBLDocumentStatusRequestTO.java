package org.dcsa.edocumentation.transferobjects.unofficial;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangeEBLDocumentStatusRequestTO(
  @NotNull
  @EnumSubset(anyOf = "PENU,PENA,APPR,ISSU,SURR,VOID")
  EblDocumentStatus documentStatus,
  @Size(max = 250)
  String reason
) {
  @Builder(toBuilder = true)
  public ChangeEBLDocumentStatusRequestTO { }
}
