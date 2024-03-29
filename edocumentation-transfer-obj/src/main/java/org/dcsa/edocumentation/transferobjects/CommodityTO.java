package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;
import org.dcsa.skernel.infrastructure.validation.DateRange;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@AllOrNone({"cargoGrossVolume", "cargoGrossVolumeUnit"})
@DateRange(startField = "exportLicenseIssueDate", endField = "exportLicenseExpiryDate")
public record CommodityTO(
  @NotBlank @Size(max = 550)
  String commodityType,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @Size(max = 100)
  String commoditySubreference,

  @JsonProperty("HSCodes")
  List<@Pattern(regexp = "^[0-9]+$") @Size(min = 6, max = 10) String> hsCodes,

  @NotNull
  Float cargoGrossWeight,

  @NotNull
  WeightUnit cargoGrossWeightUnit,

  Float cargoGrossVolume,

  VolumeUnit cargoGrossVolumeUnit,

  @Valid OuterPackagingTO outerPackaging,

  LocalDate exportLicenseIssueDate,

  LocalDate exportLicenseExpiryDate
) {
  @Builder(toBuilder = true)
  public CommodityTO { }
}
