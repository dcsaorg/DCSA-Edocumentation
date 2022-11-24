package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettings.*;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = EblFreezerTO.class, name = "FREZ"),
  @JsonSubTypes.Type(value = EblSuperFreezerTO.class, name = "SUPR"),
  @JsonSubTypes.Type(value = EblRefrigeratedTO.class, name = "REFR"),
  @JsonSubTypes.Type(value = EblControlledAtmosphereTO.class, name = "CONA")
}
)
public sealed interface ShippingInstructionActiveReeferSettings permits EblFreezerTO, EblSuperFreezerTO, EblRefrigeratedTO, EblControlledAtmosphereTO{

  record EblFreezerTO(
    @EnumSubset(anyOf = "FREZ")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettings {
    @Builder public EblFreezerTO{}
  }

  record EblSuperFreezerTO(
    @EnumSubset(anyOf = "SUPR")
    ReeferType type,

    @Size(max = 500)
    String productName,

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

    @NotNull
    Integer temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements ShippingInstructionActiveReeferSettings {
    @Builder public EblSuperFreezerTO{}
  }

  record EblRefrigeratedTO(
    @EnumSubset(anyOf = "REFR")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettings {
    @Builder public EblRefrigeratedTO{}
  }

  record EblControlledAtmosphereTO(
    @EnumSubset(anyOf = "CONA")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettings {
    @Builder public EblControlledAtmosphereTO{}
  }
}
