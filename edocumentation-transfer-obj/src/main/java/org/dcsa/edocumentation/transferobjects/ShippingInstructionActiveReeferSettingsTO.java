package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.*;
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
public sealed interface ShippingInstructionActiveReeferSettingsTO permits EblFreezerTO, EblSuperFreezerTO, EblRefrigeratedTO, EblControlledAtmosphereTO{

  record EblFreezerTO(
    @EnumSubset(anyOf = "FREZ")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblFreezerTO{}
  }

  record EblSuperFreezerTO(
    @EnumSubset(anyOf = "SUPR")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblSuperFreezerTO{}
  }

  record EblRefrigeratedTO(
    @EnumSubset(anyOf = "REFR")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblRefrigeratedTO{}
  }

  record EblControlledAtmosphereTO(
    @EnumSubset(anyOf = "CONA")
    ReeferType type,

    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblControlledAtmosphereTO{}
  }
}
