package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;

import java.util.List;

@UtilityClass
public class RequestedEquipmentDataFactory {

  /* TODO fix
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
   */

  public static List<RequestedEquipmentTO> requestedEquipmentTOList() {
    EquipmentTO equipmentTO1 = EquipmentTO.builder()
      .equipmentReference("Equipment_Ref_01")
      .build();

    EquipmentTO equipmentTO2 = EquipmentTO.builder()
      .equipmentReference("Equipment_Ref_02")
      .build();
    return List.of(
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of(equipmentTO1))
        .isoEquipmentCode("GP22")
        .units(1)
        .isShipperOwned(true)
        .build(),
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of(equipmentTO2))
        .isoEquipmentCode("GP22")
        .units(1)
        .isShipperOwned(false)
        .build()
    );
  }

  /* TODO fix
  public static List<RequestedEquipment> requestedEquipmentList() {
    List<Equipment> equipments = EquipmentDataFactory.equipmentList();
    return List.of(
      RequestedEquipment.builder()
        .isShipperOwned(true)
        .sizeType("GP22")
        .units(1)
        .equipments(Set.of(equipments.get(0)))
        .build(),
      RequestedEquipment.builder()
        .isShipperOwned(false)
        .sizeType("GP22")
        .units(1)
        .equipments(Set.of(equipments.get(1)))
        .build()
    );
  }
   */
}
