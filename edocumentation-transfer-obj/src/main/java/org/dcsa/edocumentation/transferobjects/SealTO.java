package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.SealSourceCode;
import org.dcsa.edocumentation.transferobjects.enums.SealTypeCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record SealTO(
  @Size(max = 15)
  @NotNull(message = "Seal number is required.")
  String sealNumber,

  SealSourceCode sealSource,

  SealTypeCode sealType
) {
  @Builder
  public SealTO { }
}
