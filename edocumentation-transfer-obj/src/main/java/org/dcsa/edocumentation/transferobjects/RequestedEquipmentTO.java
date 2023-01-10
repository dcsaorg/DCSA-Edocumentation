package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record RequestedEquipmentTO(
  @NotBlank @Size(max = 4)
  @JsonProperty("ISOEquipmentCode")
  String isoEquipmentCode,

  Float tareWeight,

  WeightUnit tareWeightUnit,

  @NotNull
  Float units,

  List<@NotBlank @Size(max = 15) String> equipmentReferences,

  @NotNull
  Boolean isShipperOwned,

  @Size(max = 100)
  String commodityRequestedEquipmentLink,

  BookingActiveReeferSettingsTO activeReeferSettings
) {
  @Builder(toBuilder = true)
  public RequestedEquipmentTO { }
}
