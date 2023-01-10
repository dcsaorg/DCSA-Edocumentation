package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.SealSourceCode;
import org.dcsa.edocumentation.transferobjects.enums.SealTypeCode;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SealTO(
  @Size(max = 15)
  @NotNull(message = "Seal number is required.")
  String number,

  SealSourceCode source,

  SealTypeCode type
) {
  @Builder
  public SealTO { }
}
