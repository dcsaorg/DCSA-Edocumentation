package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

public record ActiveReeferSettingsTO(
  Double temperatureMin,

  Double temperatureMax,

  TemperatureUnit temperatureUnit,

  Double humidityMin,

  Double humidityMax,

  Double ventilationMin,

  Double ventilationMax
) {
  @Builder
  public ActiveReeferSettingsTO {}
}
