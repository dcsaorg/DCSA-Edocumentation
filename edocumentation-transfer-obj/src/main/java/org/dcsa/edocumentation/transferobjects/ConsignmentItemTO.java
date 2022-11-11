package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.RequiredIfOther;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RequiredIfOther(ifNotNull = "volume", thenNotNull = "volumeUnit")
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
