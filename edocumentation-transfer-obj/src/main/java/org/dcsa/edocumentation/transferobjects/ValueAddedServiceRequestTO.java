package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ValueAddedServiceCode;

import javax.validation.constraints.NotNull;

public record ValueAddedServiceRequestTO(
  @NotNull
  ValueAddedServiceCode valueAddedServiceCode
) {
  @Builder(toBuilder = true)
  public ValueAddedServiceRequestTO { }
}
