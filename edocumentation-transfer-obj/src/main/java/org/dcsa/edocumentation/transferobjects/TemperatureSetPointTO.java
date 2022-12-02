package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

public record TemperatureSetPointTO(
  Float temperature,
  TemperatureUnit temperatureUnit,
  Float humidity,
  Float airExchange,
  AirExchangeUnit airExchangeUnit,
  Float daysPriorToDischarge
) {
  @Builder
  public TemperatureSetPointTO {}
}
