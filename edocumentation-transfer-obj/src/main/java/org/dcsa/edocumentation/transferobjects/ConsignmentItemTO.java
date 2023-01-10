package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@AllOrNone({"volume", "volumeUnit"})
public record ConsignmentItemTO(
  @Size(max = 35)
  String carrierBookingReference,

  @NotNull
  Double weight,

  @NotNull
  WeightUnit weightUnit,

  Double volume,

  VolumeUnit volumeUnit,

  @NotBlank
  String descriptionOfGoods,

  @Size(max = 10)
  @JsonProperty("HSCode")
  String hsCode,

  @NotEmpty
  @Valid
  List<CargoItemTO> cargoItems,

  @Valid
  List<ReferenceTO> references
) {
  @Builder
  public ConsignmentItemTO{}
}
