package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgFreezerTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgSuperFreezerTO;
import org.dcsa.edocumentation.transferobjects.ControlledAtmosphereSetPointTO;
import org.dcsa.edocumentation.transferobjects.TemperatureSetPointTO;
import org.dcsa.edocumentation.transferobjects.enums.AirExchangeUnit;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

import java.util.List;

@UtilityClass
public class BookingActiveReeferSettingsDataFactory {

  public BkgFreezerTO bkgFreezer() {
    return BkgFreezerTO.builder()
        .type(ReeferType.FREZ)
        .productName("DCSA Freezer product")
        .isGeneratorSetRequired(true)
        .isPreCoolingRequired(true)
        .isColdTreatmentRequired(true)
        .isHotStuffingAllowed(true)
        .isHighValueCargo(true)
        .isTracingRequired(true)
        .isMonitoringRequired(true)
        .extraMaterial("extra material")
        .temperatureSetpoint(-10)
        .temperatureUnit(TemperatureUnit.CEL)
        .build();
  }

  public ActiveReeferSettings bkgFreezerEntity() {
    return ActiveReeferSettings.builder()
      .productName("DCSA Freezer product")
      .isPreCoolingRequired(true)
      .isColdTreatmentRequired(true)
      .isHotStuffingAllowed(true)
      .isHighValueCargo(true)
      .isTracingRequired(true)
      .isMonitoringRequired(true)
      .extraMaterial("extra material")
      .build();
  }

  public BkgSuperFreezerTO bkgSuperFreezer() {
    return BkgSuperFreezerTO.builder()
      .type(ReeferType.SUPR)
      .productName("DCSA Super Freezer product")
      .isGeneratorSetRequired(true)
      .isPreCoolingRequired(true)
      .isColdTreatmentRequired(true)
      .isHotStuffingAllowed(true)
      .isHighValueCargo(true)
      .isCargoProbe1Required(true)
      .isCargoProbe2Required(true)
      .isCargoProbe3Required(true)
      .isCargoProbe4Required(true)
      .isTracingRequired(true)
      .isMonitoringRequired(true)
      .extraMaterial("extra material")
      .temperatureSetpoint(-40)
      .temperatureUnit(TemperatureUnit.CEL)
      .build();
  }

  public BkgRefrigeratedTO bkgRefrigerated() {
    return BkgRefrigeratedTO.builder()
        .type(ReeferType.REFR)
        .productName("DCSA refrigerated product")
        .isGeneratorSetRequired(false)
        .isPreCoolingRequired(true)
        .isColdTreatmentRequired(true)
        .isHotStuffingAllowed(false)
        .isHighValueCargo(false)
        .isVentilationOpen(false)
        .isCargoProbe1Required(true)
        .isCargoProbe2Required(true)
        .isCargoProbe3Required(true)
        .isCargoProbe4Required(true)
        .isDrainholesOpen(false)
        .isBulbMode(true)
        .isTracingRequired(true)
        .isMonitoringRequired(true)
        .extraMaterial("extra material")
        .setpoints(List.of(TemperatureSetPointTO.builder()
            .airExchange(10.2F)
            .airExchangeUnit(AirExchangeUnit.MQH)
            .humidity(65.4F)
            .temperature(6)
            .temperatureUnit(TemperatureUnit.CEL)
            .daysPriorToDischarge(10F)
          .build()))
        .build();
  }

  public BkgControlledAtmosphereTO bkgControlledAtmosphere() {
    return BkgControlledAtmosphereTO.builder()
      .type(ReeferType.CONA)
      .productName("DCSA controlled atmosphere product")
      .isGeneratorSetRequired(true)
      .isPreCoolingRequired(false)
      .isColdTreatmentRequired(false)
      .isHotStuffingAllowed(false)
      .isHighValueCargo(false)
      .isCargoProbe1Required(false)
      .isCargoProbe2Required(false)
      .isCargoProbe3Required(false)
      .isCargoProbe4Required(false)
      .isTracingRequired(true)
      .isMonitoringRequired(true)
      .extraMaterial("extra material")
      .setpoints(List.of(ControlledAtmosphereSetPointTO.builder()
          .airExchange(10.3F)
          .airExchangeUnit(AirExchangeUnit.MQH)
          .humidity(54F)
          .co2(32.4F)
          .o2(23F)
          .temperature(23)
          .temperatureUnit(TemperatureUnit.CEL)
          .daysPriorToDischarge(12.3F)
        .build()))
      .build();
  }
}
