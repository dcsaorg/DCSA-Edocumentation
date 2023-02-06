package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.ISO6346EquipmentReference;

import java.util.List;

public record RequestedEquipmentTO(
  @NotBlank @Size(max = 4)
  @JsonProperty("ISOEquipmentCode")
  String isoEquipmentCode,

  Float tareWeight,

  WeightUnit tareWeightUnit,

  @NotNull
  Float units,

  List<@NotBlank @Size(max = 15) @ISO6346EquipmentReference String> equipmentReferences,

  @NotNull
  Boolean isShipperOwned,

  @Size(max = 100)
  String commodityRequestedEquipmentLink,

  @Valid
  ActiveReeferSettingsTO activeReeferSettings
) {
  @Builder(toBuilder = true)
  public RequestedEquipmentTO { }
}
