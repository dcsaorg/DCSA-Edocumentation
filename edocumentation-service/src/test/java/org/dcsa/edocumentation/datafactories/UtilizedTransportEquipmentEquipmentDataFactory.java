package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.SealTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.dcsa.edocumentation.transferobjects.enums.SealSourceCode;
import org.dcsa.edocumentation.transferobjects.enums.SealTypeCode;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import java.util.List;

@UtilityClass
public class UtilizedTransportEquipmentEquipmentDataFactory {

  public UtilizedTransportEquipmentTO singleShipperOwned() {
    return UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(true)
      .equipment(EquipmentTO.builder()
        .equipmentReference("Equipment_Ref_01")
        .isoEquipmentCode("22G2")
        .tareWeight(100.3)
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .cargoGrossVolume(100.0)
      .cargoGrossVolumeUnit(VolumeUnit.MTQ)
      .numberOfPackages(1)
      .build();
  }

  public UtilizedTransportEquipment singleShipperOwnedDao() {
    return UtilizedTransportEquipment.builder()
      .isShipperOwned(true)
      .equipment(Equipment.builder()
        .equipmentReference("Equipment_Ref_01")
        .isoEquipmentCode("22G2")
        .tareWeight(100.3f)
        .weightUnit(org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit.KGM)
        .build())
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit.KGM)
      .build();
  }

  public List<UtilizedTransportEquipmentTO> multipleShipperOwned() {
    UtilizedTransportEquipmentTO utEq1 = UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(true)
      .equipment(EquipmentTO.builder()
        .equipmentReference("Equipment_Ref_01")
        .isoEquipmentCode("22G2")
        .tareWeight(100.3)
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .build();

    UtilizedTransportEquipmentTO utEq2 = UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(true)
      .equipment(EquipmentTO.builder()
        .equipmentReference("Equipment_Ref_02")
        .isoEquipmentCode("22G2")
        .tareWeight(100.3)
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .build();

    return List.of(utEq1, utEq2);
  }

  public UtilizedTransportEquipmentTO singleCarrierOwned() {
    return UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(false)
      .equipment(EquipmentTO.builder()
        .equipmentReference("CARR_EQ_REF_01")
        .isoEquipmentCode("22G2")
        .tareWeight(100.3)
        .weightUnit(WeightUnit.KGM)
        .build())
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .build();
  }

  public UtilizedTransportEquipmentTO singleCarrierOwnedWithSeal() {
    return UtilizedTransportEquipmentTO.builder()
        .isShipperOwned(false)
        .equipment(
            EquipmentTO.builder()
                .equipmentReference("CARR_EQ_REF_01")
                .isoEquipmentCode("22G2")
                .tareWeight(100.3)
                .weightUnit(WeightUnit.KGM)
                .build())
        .cargoGrossWeight(100.3)
        .cargoGrossWeightUnit(WeightUnit.KGM)
        .seals(List.of(SealTO.builder()
            .number("12345")
            .type(SealTypeCode.BLT)
            .source(SealSourceCode.CAR)
          .build()))
        .build();
  }

  public List<UtilizedTransportEquipmentTO> multipleCarrierOwned() {
    UtilizedTransportEquipmentTO utEq1 = UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(false)
      .equipmentReference("CARR_EQ_REF_01")
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .build();

    UtilizedTransportEquipmentTO utEq2 = UtilizedTransportEquipmentTO.builder()
      .isShipperOwned(false)
      .equipmentReference("CARR_EQ_REF_02")
      .cargoGrossWeight(100.3)
      .cargoGrossWeightUnit(WeightUnit.KGM)
      .build();

    return List.of(utEq1, utEq2);
  }
}
