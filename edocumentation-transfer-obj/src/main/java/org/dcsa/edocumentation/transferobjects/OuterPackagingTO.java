package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record OuterPackagingTO(

  @Pattern(regexp = "^[A-Z0-9]{2}$")
  String packageCode,

  @Pattern(regexp = "^[A-Z0-9]{1,5}$")
  String imoPackagingCode,

  @Min(1)
  Integer numberOfPackages,

  @Size(max = 100)
  String description

  //TODO:: Dangerousgoods must be added later.
) {
  @Builder(toBuilder = true)
  public OuterPackagingTO { }
}
