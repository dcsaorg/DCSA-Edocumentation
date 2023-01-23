package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;

import jakarta.validation.constraints.Size;

public record ServiceTO(
  @Size(max = 11)
  String carrierServiceCode,

  @Size(max = 50)
  String carrierServiceName,

  @Size(max = 8)
  String tradelaneId,

  @UniversalServiceReference
  String universalServiceReference
) {
  @Builder
  public ServiceTO { }
}
