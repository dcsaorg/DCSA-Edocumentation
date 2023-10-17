package org.dcsa.edocumentation.transferobjects.unofficial;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

public record ChangeEBLDocumentStatusRequestTO(

  @NotNull
  @EnumSubset(anyOf = "PENDING UPDATE,PENDING APPROVAL,APPROVED,ISSUED,SURRENDERED,VOID")
  String documentStatus
) {
  @Builder(toBuilder = true)
  public ChangeEBLDocumentStatusRequestTO { }
}
