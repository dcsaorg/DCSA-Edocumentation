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
    boolean isGeneratorSetRequired,

    @NotNull
    boolean isPreCoolingRequired,

    @NotNull
    boolean isColdTreatmentRequired,

    @NotNull
    boolean isHotStuffingAllowed,

    @NotNull
    boolean isHighValueCargo,

    @NotNull
    boolean isTracingRequired,

    @NotNull
    boolean isMonitoringRequired,

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
    boolean isGeneratorSetRequired,

    @NotNull
    boolean isPreCoolingRequired,

    @NotNull
    boolean isColdTreatmentRequired,

    @NotNull
    boolean isHotStuffingAllowed,

    @NotNull
    boolean isHighValueCargo,

    @NotNull
    boolean isTracingRequired,

    @NotNull
    boolean isMonitoringRequired,

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
    boolean isGeneratorSetRequired,

    @NotNull
    boolean isPreCoolingRequired,

    @NotNull
    boolean isColdTreatmentRequired,

    @NotNull
    boolean isHotStuffingAllowed,

    @NotNull
    boolean isHighValueCargo,

    @NotNull
    boolean isVentilationOpen,

    @NotNull
    boolean isCargoProbe1Required,

    @NotNull
    boolean isCargoProbe2Required,

    @NotNull
    boolean isCargoProbe3Required,

    @NotNull
    boolean isCargoProbe4Required,

    @NotNull
    boolean isDrainholesOpen,

    @NotNull
    boolean isBulbMode,

    @NotNull
    boolean isTracingRequired,

    @NotNull
    boolean isMonitoringRequired,

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
    boolean isGeneratorSetRequired,

    @NotNull
    boolean isPreCoolingRequired,

    @NotNull
    boolean isColdTreatmentRequired,

    @NotNull
    boolean isHotStuffingAllowed,

    @NotNull
    boolean isHighValueCargo,

    @NotNull
    boolean isCargoProbe1Required,

    @NotNull
    boolean isCargoProbe2Required,

    @NotNull
    boolean isCargoProbe3Required,

    @NotNull
    boolean isCargoProbe4Required,

    @NotNull
    boolean isTracingRequired,

    @NotNull
    boolean isMonitoringRequired,

    @Size(max = 500)
    String extraMaterial,

    @NotEmpty
    List<ControlledAtmosphereSetPointTO> setpoints
  ) implements BookingActiveReeferSettingsTO {}

}




