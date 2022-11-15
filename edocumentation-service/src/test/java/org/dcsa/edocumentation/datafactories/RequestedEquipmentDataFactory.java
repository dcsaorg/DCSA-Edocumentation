package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class RequestedEquipmentDataFactory {

  public static RequestedEquipment singleRequestedEquipment() {
    return RequestedEquipment.builder()
        .id(UUID.randomUUID())
        .confirmedEquipmentSizetype("confirmed size type")
        .isShipperOwned(true)
        .sizeType("size Type")
        .units(1)
        .equipments(
            Set.of(
                Equipment.builder()
                    .equipmentReference("Equipment_Ref_01")
                    .tareWeight(100F)
                    .weightUnit(WeightUnit.KGM)
                    .build()))
        .build();
  }

  public static List<RequestedEquipmentTO> requestedEquipmentTOList() {
    return List.of(
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of("Equipment_Ref_01"))
        .sizeType("size Type 1")
        .units(1)
        .isShipperOwned(true)
        .build(),
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of("Equipment_Ref_02"))
        .sizeType("size Type 2")
        .units(1)
        .isShipperOwned(false)
        .build()
    );
  }


  public static List<RequestedEquipment> requestedEquipmentList() {
    List<Equipment> equipments = EquipmentDataFactory.equipmentList();
    return List.of(
      RequestedEquipment.builder()
        .isShipperOwned(true)
        .sizeType("size Type 1")
        .units(1)
        .equipments(Set.of(equipments.get(0)))
        .build(),
      RequestedEquipment.builder()
        .isShipperOwned(false)
        .sizeType("size Type 2")
        .units(1)
        .equipments(Set.of(equipments.get(1)))
        .build()
    );
  }
}
