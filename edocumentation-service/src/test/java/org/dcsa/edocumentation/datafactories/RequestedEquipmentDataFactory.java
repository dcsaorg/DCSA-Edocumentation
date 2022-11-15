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

  public RequestedEquipment singleRequestedEquipment() {
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

  public List<RequestedEquipmentTO> requestedEquipmentTOList() {
    return List.of(
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of("Equipment_Ref_01", "Equipment_Ref_02", "Equipment_Ref_03"))
        .sizeType("size Type 1")
        .units(3)
        .isShipperOwned(true)
        .build(),
      RequestedEquipmentTO.builder()
        .equipmentReferences(List.of("Equipment_Ref_02", "Equipment_Ref_03", "Equipment_Ref_04"))
        .sizeType("size Type 2")
        .units(3)
        .isShipperOwned(true)
        .build()
    );
  }

  public Set<String> equipmentReferenceList() {
    return Set.of("Equipment_Ref_01", "Equipment_Ref_02", "Equipment_Ref_03", "Equipment_Ref_04");
  }

  public List<Equipment> equipmentList() {
    return List.of(
      Equipment.builder()
        .equipmentReference("Equipment_Ref_01")
        .tareWeight(100F)
        .weightUnit(WeightUnit.KGM)
        .build(),
      Equipment.builder()
        .equipmentReference("Equipment_Ref_02")
        .tareWeight(314F)
        .weightUnit(WeightUnit.KGM)
        .build(),
      Equipment.builder()
        .equipmentReference("Equipment_Ref_03")
        .tareWeight(99F)
        .weightUnit(WeightUnit.KGM)
        .build(),
      Equipment.builder()
        .equipmentReference("Equipment_Ref_04")
        .tareWeight(5F)
        .weightUnit(WeightUnit.KGM)
        .build()
    );
  }

  public List<RequestedEquipment> requestedEquipmentList() {
    List<Equipment> equipments = equipmentList();
    return List.of(
      RequestedEquipment.builder()
        .isShipperOwned(true)
        .sizeType("size Type 1")
        .units(3)
        .equipments(Set.of(equipments.get(0), equipments.get(1), equipments.get(2)))
        .build(),
      RequestedEquipment.builder()
        .isShipperOwned(true)
        .sizeType("size Type 2")
        .units(3)
        .equipments(Set.of(equipments.get(1), equipments.get(2), equipments.get(3)))
        .build()
    );
  }
}
