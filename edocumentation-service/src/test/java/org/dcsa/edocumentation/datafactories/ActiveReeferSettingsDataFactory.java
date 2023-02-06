package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.transferobjects.ActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

@UtilityClass
public class ActiveReeferSettingsDataFactory {
  public static ActiveReeferSettingsTO activeReeferSettingsTO() {
    return ActiveReeferSettingsTO.builder()
      .isGeneratorSetRequired(false)
      .isPreCoolingRequired(false)
      .isColdTreatmentRequired(false)
      .isVentilationOpen(false)
      .isDrainholesOpen(false)
      .isBulbMode(false)
      .temperatureSetpoint(21f)
      .temperatureUnit(TemperatureUnit.CEL)
      .o2Setpoint(12f)
      .co2Setpoint(13f)
      .humiditySetpoint(14f)
      .airExchangeSetpoint(17f)
      .airExchangeUnit(AirExchangeUnit.MQH)
      .build();
  }

  public static ActiveReeferSettings activeReeferSettings() {
    return ActiveReeferSettings.builder()
      .isGeneratorSetRequired(false)
      .isPreCoolingRequired(false)
      .isColdTreatmentRequired(false)
      .isVentilationOpen(false)
      .isDrainholesOpen(false)
      .isBulbMode(false)
      .temperatureSetpoint(21f)
      .temperatureUnit(org.dcsa.edocumentation.domain.persistence.entity.enums.TemperatureUnit.CEL)
      .o2Setpoint(12f)
      .co2Setpoint(13f)
      .humiditySetpoint(14f)
      .airExchangeSetpoint(17f)
      .airExchangeUnit(org.dcsa.edocumentation.domain.persistence.entity.enums.AirExchangeUnit.MQH)
      .build();
  }
}
