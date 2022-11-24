package org.dcsa.edocumentation.transferobjects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AirExchangeUnit {
  MQH("Cubic metre per hour"),
  FQH("Cubic foot per hour");

  private final String value;
}
