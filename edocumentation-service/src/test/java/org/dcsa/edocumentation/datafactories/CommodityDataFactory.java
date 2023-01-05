package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.transferobjects.CommodityTO;

import java.time.LocalDate;
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
      .numberOfPackages(1)
      .requestedEquipments(Set.of(RequestedEquipmentDataFactory.singleRequestedEquipment()))
      .hsCode("123");
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
      .numberOfPackages(1)
      .hsCode("123")
      .commodityRequestedEquipmentLink("commodityRequestedEquipmentLink")
      .requestedEquipments(RequestedEquipmentDataFactory.requestedEquipmentTOList())
      .build();
  }
}
