package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;

import javax.validation.constraints.Size;

public record ServiceTO(
  @Size(max = 5)
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
