package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.ControlledAtmosphereSetPointTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblFreezerTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblSuperFreezerTO;
import org.dcsa.edocumentation.transferobjects.TemperatureSetPointTO;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

import java.util.List;

@UtilityClass
public class ShippingInstructionActiveReeferSettingsDataFactory {
  public EblFreezerTO eblFreezer() {
    return EblFreezerTO.builder()
      .type(ReeferType.FREZ)
      .productName("DCSA Freezer")
      .isTracingRequired(false)
      .isMonitoringRequired(false)
      .extraMaterial("extra material")
      .temperatureSetpoint(-5)
      .temperatureUnit(TemperatureUnit.CEL)
      .build();
  }

  public EblSuperFreezerTO eblSuperFreezer() {
    return EblSuperFreezerTO.builder()
      .type(ReeferType.SUPR)
      .productName("DCSA Super Freezer")
      .isCargoProbe1Required(false)
      .isCargoProbe2Required(false)
      .isCargoProbe3Required(false)
      .isCargoProbe4Required(false)
      .isTracingRequired(false)
      .isMonitoringRequired(false)
      .extraMaterial("extra material")
      .temperatureSetpoint(-70)
      .temperatureUnit(TemperatureUnit.CEL)
      .build();
  }

  public EblRefrigeratedTO eblRefrigerated() {
    return EblRefrigeratedTO.builder()
      .type(ReeferType.REFR)
      .productName("DCSA refrigerated")
      .isVentilationOpen(true)
      .isCargoProbe1Required(false)
      .isCargoProbe2Required(false)
      .isCargoProbe3Required(false)
      .isCargoProbe4Required(false)
      .isDrainholesOpen(false)
      .isBulbMode(false)
      .isTracingRequired(false)
      .isMonitoringRequired(false)
      .extraMaterial("extra material")
      .setpoints(List.of(TemperatureSetPointTO.builder()
          .temperature(5)
          .temperatureUnit(TemperatureUnit.CEL)
          .humidity(40F)
          .airExchangeUnit(AirExchangeUnit.MQH)
          .airExchange(23F)
          .daysPriorToDischarge(5F)
        .build()))
      .build();
  }

  public EblControlledAtmosphereTO eblControlledAtmosphere() {
    return EblControlledAtmosphereTO.builder()
      .type(ReeferType.CONA)
      .productName("DCSA Controlled Atmosphere")
      .isCargoProbe1Required(false)
      .isCargoProbe2Required(false)
      .isCargoProbe3Required(false)
      .isCargoProbe4Required(false)
      .isTracingRequired(false)
      .isMonitoringRequired(false)
      .extraMaterial("extra material")
      .setpoints(List.of(ControlledAtmosphereSetPointTO.builder()
          .temperature(11)
          .temperatureUnit(TemperatureUnit.CEL)
          .o2(12F)
          .co2(43F)
          .humidity(54F)
          .airExchange(23F)
          .airExchangeUnit(AirExchangeUnit.MQH)
          .daysPriorToDischarge(234F)
        .build()))
      .build();
  }
}
