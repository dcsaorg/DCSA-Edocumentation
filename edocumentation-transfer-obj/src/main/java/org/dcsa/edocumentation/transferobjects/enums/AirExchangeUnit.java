package org.dcsa.edocumentation.transferobjects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AirExchangeUnit {
  MQH("Cubic metre per hour"),
  TWO_K("Cubic foot per hour");

  private final String value;
}
