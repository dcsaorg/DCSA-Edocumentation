package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public class CommodityDataFactory {

  public Commodity singleCommodity() {
    return Commodity.builder()
        .id(UUID.randomUUID())
        .commodityType("commodity type")
        .cargoGrossVolume(100.0)
        .cargoGrossVolumeUnit(VolumeUnit.MTQ)
        .cargoGrossWeight(323.32)
        .cargoGrossWeightUnit(WeightUnit.KGM)
        .exportLicenseIssueDate(LocalDate.now())
        .exportLicenseExpiryDate(LocalDate.now().plusMonths(6))
        .numberOfPackages(1)
        .hsCode("123")
        .build();
  }
}
