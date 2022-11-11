package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ReferenceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record ReferenceTO(
  @NotNull
  ReferenceType type,

  @NotBlank @Size(max = 100)
  String value
) {
  @Builder(toBuilder = true)
  public ReferenceTO { }
}
