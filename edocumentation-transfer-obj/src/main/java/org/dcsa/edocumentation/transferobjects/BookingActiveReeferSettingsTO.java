package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO.*;
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
    @EnumSubset(anyOf = "FREZ")
    ReeferType type,

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
    Integer temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgFreezerTO {}
  }

  record BkgSuperFreezerTO(
    @EnumSubset(anyOf = "SUPR")
    ReeferType type,

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
    Integer temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements BookingActiveReeferSettingsTO {
    @Builder
    public BkgSuperFreezerTO {}
  }

  record BkgRefrigeratedTO(
    @EnumSubset(anyOf = "REFR")
    ReeferType type,

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
    @EnumSubset(anyOf = "CONA")
    ReeferType type,

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
  ) implements BookingActiveReeferSettingsTO {}

}




