package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

@AllOrNone({"airExchangeSetpoint", "airExchangeUnit"})
public record ActiveReeferSettingsTO(
  @NotNull
  Boolean isGeneratorSetRequired,

  @NotNull
  Boolean isPreCoolingRequired,

  @NotNull
  Boolean isColdTreatmentRequired,

  @NotNull
  Boolean isVentilationOpen,

  @NotNull
  Boolean isDrainholesOpen,

  @NotNull
  Boolean isBulbMode,

  @NotNull
  Float temperatureSetpoint,

  @NotNull
  TemperatureUnit temperatureUnit,

  Float o2Setpoint,

  Float co2Setpoint,

  Float humiditySetpoint,

  Float airExchangeSetpoint,

  AirExchangeUnit airExchangeUnit
) {
  @Builder
  public ActiveReeferSettingsTO { }
}
