package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SealTO(
  @Size(max = 15)
  @NotNull(message = "Seal number is required.")
  String number,

  @Size(max = 5)
  String source,

  @Size(max = 5)
  String type
) {
  @Builder
  public SealTO { }
}
