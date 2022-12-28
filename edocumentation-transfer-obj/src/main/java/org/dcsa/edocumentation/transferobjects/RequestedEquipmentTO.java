package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record RequestedEquipmentTO(
  @NotBlank @Size(max = 4)
  @JsonProperty("ISOEquipmentCode")
  String isoEquipmentCode,

  Float tareWeight,

  WeightUnit tareWeightUnit,

  @NotNull
  Integer units,

  List<@NotBlank @Size(max = 15) String> equipmentReferences,

  @NotNull
  Boolean isShipperOwned,

  @Size(max = 100)
  String commodityRequestedEquipmentLink

) {
  @Builder(toBuilder = true)
  public RequestedEquipmentTO { }
}
