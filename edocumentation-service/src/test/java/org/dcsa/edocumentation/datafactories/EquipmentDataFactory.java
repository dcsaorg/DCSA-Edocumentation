package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@UtilityClass
public class EquipmentDataFactory {
  public static Set<String> equipmentReferenceList() {
    return Set.of("Equipment_Ref_01", "Equipment_Ref_02");
  }

  public static List<Equipment> equipmentList() {
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
        .build()
    );
  }

  public static Map<String, Equipment> equipmentMap() {
    return equipmentList().stream()
      .collect(Collectors.toMap(Equipment::getEquipmentReference, Function.identity()));
  }
}
