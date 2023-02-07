package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.dcsa.skernel.infrastructure.validation.ISO6346EquipmentReference;

@AllOrNone({"tareWeight", "weightUnit"})
public record EquipmentTO(
  @NotNull(message = "EquipmentReference is required.")
  @ISO6346EquipmentReference
  String equipmentReference,

  @JsonProperty("ISOEquipmentCode")
  @Size(max = 4)
  String isoEquipmentCode,

  Double tareWeight,

  WeightUnit weightUnit
) {
  @Builder
  public EquipmentTO{}
}
