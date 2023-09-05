package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record ReferenceTO(
  @NotNull
  @Size(max = 3)
  String type,

  @NotBlank @Size(max = 100)
  String value
) {
  @Builder(toBuilder = true)
  public ReferenceTO { }
}
