package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

public record ServiceTO(
  String carrierServiceCode,
  String carrierServiceName,
  String tradelaneId,
  String universalServiceReference
) {
  @Builder
  public ServiceTO { }
}
