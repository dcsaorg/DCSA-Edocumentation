package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

public record ControlledAtmosphereSetPointTO(
  Integer temperature,
  TemperatureUnit temperatureUnit,
  Float o2,
  Float co2,
  Float humidity,
  Float airExchange,
  AirExchangeUnit airExchangeUnit,
  Float daysPriorToDischarge
) {
  @Builder
  public ControlledAtmosphereSetPointTO {}
}
