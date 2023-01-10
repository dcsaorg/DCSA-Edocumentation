package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgFreezerTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.BkgSuperFreezerTO;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = BkgFreezerTO.class, name = "FREZ"),
  @JsonSubTypes.Type(value = BkgSuperFreezerTO.class, name = "SUPR"),
  @JsonSubTypes.Type(value = BkgRefrigeratedTO.class, name = "REFR"),
  @JsonSubTypes.Type(value = BkgControlledAtmosphereTO.class, name = "CONA")
}
)
public sealed interface BookingActiveReeferSettingsTO permits BkgFreezerTO, BkgSuperFreezerTO, BkgControlledAtmosphereTO, BkgRefrigeratedTO {

  record BkgFreezerTO(
    @Size(max = 500)
    String productName,

    @NotNull
    Boolean isGeneratorSetRequired,

    @NotNull
    Boolean isPreCoolingRequired,

    @NotNull
    Boolean isColdTreatmentRequired,

    @NotNull
    Boolean isHotStuffingAllowed,

    @NotNull
    Boolean isHighValueCargo,

    @NotNull
    Boolean isTracingRequired,

    @NotNull
    Boolean isMonitoringRequired,

    @Size(max = 500)
    String extraMaterial,

    @NotNull
    Float temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgFreezerTO {}
  }

  record BkgSuperFreezerTO(
    @Size(max = 500)
    String productName,

    @NotNull
    Boolean isGeneratorSetRequired,

    @NotNull
    Boolean isPreCoolingRequired,

    @NotNull
    Boolean isColdTreatmentRequired,

    @NotNull
    Boolean isHotStuffingAllowed,

    @NotNull
    Boolean isHighValueCargo,

    @NotNull
    Boolean isCargoProbe1Required,

    @NotNull
    Boolean isCargoProbe2Required,

    @NotNull
    Boolean isCargoProbe3Required,

    @NotNull
    Boolean isCargoProbe4Required,

    @NotNull
    Boolean isTracingRequired,

    @NotNull
    Boolean isMonitoringRequired,

    @Size(max = 500)
    String extraMaterial,

    @NotNull
    Float temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgSuperFreezerTO {}
  }

  record BkgRefrigeratedTO(
    @Size(max = 500)
    String productName,

    @NotNull
    Boolean isGeneratorSetRequired,

    @NotNull
    Boolean isPreCoolingRequired,

    @NotNull
    Boolean isColdTreatmentRequired,

    @NotNull
    Boolean isHotStuffingAllowed,

    @NotNull
    Boolean isHighValueCargo,

    @NotNull
    Boolean isVentilationOpen,

    @NotNull
    Boolean isCargoProbe1Required,

    @NotNull
    Boolean isCargoProbe2Required,

    @NotNull
    Boolean isCargoProbe3Required,

    @NotNull
    Boolean isCargoProbe4Required,

    @NotNull
    Boolean isDrainholesOpen,

    @NotNull
    Boolean isBulbMode,

    @NotNull
    Boolean isTracingRequired,

    @NotNull
    Boolean isMonitoringRequired,

    @Size(max = 500)
    String extraMaterial,

    @NotEmpty
    List<TemperatureSetPointTO> setpoints
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgRefrigeratedTO {}
  }

  record BkgControlledAtmosphereTO(
    @Size(max = 500)
    String productName,

    @NotNull
    Boolean isGeneratorSetRequired,

    @NotNull
    Boolean isPreCoolingRequired,

    @NotNull
    Boolean isColdTreatmentRequired,

    @NotNull
    Boolean isHotStuffingAllowed,

    @NotNull
    Boolean isHighValueCargo,

    @NotNull
    Boolean isCargoProbe1Required,

    @NotNull
    Boolean isCargoProbe2Required,

    @NotNull
    Boolean isCargoProbe3Required,

    @NotNull
    Boolean isCargoProbe4Required,

    @NotNull
    Boolean isTracingRequired,

    @NotNull
    Boolean isMonitoringRequired,

    @Size(max = 500)
    String extraMaterial,

    @NotEmpty
    List<ControlledAtmosphereSetPointTO> setpoints
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgControlledAtmosphereTO{}
  }
}




