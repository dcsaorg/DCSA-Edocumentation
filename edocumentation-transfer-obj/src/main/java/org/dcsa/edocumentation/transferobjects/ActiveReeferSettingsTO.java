package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

@AllOrNone({"airExchangeSetpoint", "airExchangeUnit"})
//TODO: Split between eBL and BKG
public record ActiveReeferSettingsTO(

  // TODO: BKG only attribute; must not be present in eBL
  @NotNull
  Boolean isGeneratorSetRequired,

  // TODO: BKG only attribute; must not be present in eBL
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
  Boolean isControlledAtmosphereRequired,

  @NotNull
  Float temperatureSetpoint,

  @NotNull
  TemperatureUnit temperatureUnit,

  @Min(0) @Max(100)
  Float o2Setpoint,

  @Min(0) @Max(100)
  Float co2Setpoint,

  @Min(0) @Max(100)
  Float humiditySetpoint,

  @Min(0)
  Float airExchangeSetpoint,

  AirExchangeUnit airExchangeUnit
) {
  @Builder
  public ActiveReeferSettingsTO { }
}
