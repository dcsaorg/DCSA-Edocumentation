package org.dcsa.edocumentation.transferobjects.unofficial;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

public record ChangeEBLDocumentStatusRequestTO(

  // FIXME: Should not support ISSU; ISSU requires more complex logic.
  @NotNull
  @EnumSubset(anyOf = "PENU,PENA,APPR,ISSU,SURR,VOID")
  EblDocumentStatus documentStatus
) {
  @Builder(toBuilder = true)
  public ChangeEBLDocumentStatusRequestTO { }
}
