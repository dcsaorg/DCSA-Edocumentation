package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ReferenceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReferenceTO(
  @NotNull
  ReferenceType type,

  @NotBlank @Size(max = 100)
  String value
) {
  @Builder(toBuilder = true)
  public ReferenceTO { }
}
