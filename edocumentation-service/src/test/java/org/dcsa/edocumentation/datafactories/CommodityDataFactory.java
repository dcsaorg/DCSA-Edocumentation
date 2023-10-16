package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.OuterPackaging;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.OuterPackagingTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class CommodityDataFactory {

  public Commodity singleCommodity() {
    return commodityBuilder()
        .id(UUID.randomUUID())
        .build();
  }

  public Commodity singleCommodityWithoutId() {
    return commodityBuilder().build();
  }

  private Commodity.CommodityBuilder commodityBuilder() {
    return Commodity.builder()
      .commodityType("commodity type")
      .cargoGrossVolume(100.0f)
      .cargoGrossVolumeUnit(VolumeUnit.MTQ)
      .cargoGrossWeight(323.32f)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .exportLicenseIssueDate(LocalDate.now())
      .exportLicenseExpiryDate(LocalDate.now().plusMonths(6))
      .outerPackaging(buildOuterPackaging())
      .hsCodes(List.of("123456"));
  }

  public CommodityTO singleCommodityTO() {
    return CommodityTO.builder()
      .commodityType("commodity type")
      .cargoGrossVolume(100.0f)
      .cargoGrossVolumeUnit(org.dcsa.edocumentation.transferobjects.enums.VolumeUnit.MTQ)
      .cargoGrossWeight(323.32f)
      .cargoGrossWeightUnit(org.dcsa.edocumentation.transferobjects.enums.WeightUnit.KGM)
      .exportLicenseIssueDate(LocalDate.now())
      .exportLicenseExpiryDate(LocalDate.now().plusMonths(6))
      .outerPackaging(buildOuterPackagingTO())
      .hsCodes(List.of("123456"))
      .build();
  }

  public OuterPackagingTO buildOuterPackagingTO() {
    return OuterPackagingTO.builder()
      .packageCode("5H")
      .imoPackagingCode("1A2")
      .numberOfPackages(1)
      .description("steel")
      .build();
  }

  public OuterPackaging buildOuterPackaging() {
    return OuterPackaging.builder()
      .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
      .packageCode("5H")
      .imoPackagingCode("1A2")
      .numberOfPackages(1)
      .description("steel")
      .build();
  }

}
