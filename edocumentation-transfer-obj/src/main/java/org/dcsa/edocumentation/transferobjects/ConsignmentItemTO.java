package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
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
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String descriptionOfGoods,

  @JsonProperty("HSCodes")
  @NotEmpty
  List<@Pattern(regexp = "^[0-9]+$") @Size(min = 6, max = 10) String> hsCodes,

  @Valid
  List<@Valid @NotNull ReferenceTO> references,

  @Valid
  List<@Valid @NotNull CustomsReferenceTO> customsReferences,
  @NotEmpty
  @Valid
  List<@Valid @NotNull CargoItemTO> cargoItems
  ) {
  @Builder
  public ConsignmentItemTO{}
}
