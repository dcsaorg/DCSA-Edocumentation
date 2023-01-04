package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;
import org.dcsa.skernel.infrastructure.validation.DateRange;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@AllOrNone({"cargoGrossVolume", "cargoGrossVolumeUnit"})
@DateRange(startField = "exportLicenseIssueDate", endField = "exportLicenseExpiryDate")
public record CommodityTO(
  @NotBlank @Size(max = 550)
  String commodityType,

  @Size(max = 10)
  @JsonProperty("HSCode")
  String hsCode,

  @NotNull
  Float cargoGrossWeight,

  @NotNull
  WeightUnit cargoGrossWeightUnit,

  Float cargoGrossVolume,

  VolumeUnit cargoGrossVolumeUnit,

  Integer numberOfPackages,

  LocalDate exportLicenseIssueDate,

  LocalDate exportLicenseExpiryDate,

  @Size(max = 100)
  String commodityRequestedEquipmentLink,

  @Valid
  List<RequestedEquipmentTO> requestedEquipments
) {
  @Builder(toBuilder = true)
  public CommodityTO { }
}
