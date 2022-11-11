package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.RequiredIfOther;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RequiredIfOther(ifNotNull = "tareWeight", thenNotNull = "weightUnit")
public record EquipmentTO(
  @NotNull(message = "EquipmentReference is required.")
  @Size(max = 15)
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
