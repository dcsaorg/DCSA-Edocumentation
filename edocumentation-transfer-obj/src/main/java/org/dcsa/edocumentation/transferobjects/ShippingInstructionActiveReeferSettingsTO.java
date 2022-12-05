package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblControlledAtmosphereTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblFreezerTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblRefrigeratedTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO.EblSuperFreezerTO;
import org.dcsa.edocumentation.transferobjects.enums.TemperatureUnit;

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
    @Size(max = 500)
    String productName,

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
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblFreezerTO{}
  }

  record EblSuperFreezerTO(
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
    Float temperatureSetpoint,

    @NotNull
    TemperatureUnit temperatureUnit
  ) implements ShippingInstructionActiveReeferSettingsTO {
    @Builder public EblSuperFreezerTO{}
  }

  record EblRefrigeratedTO(
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
