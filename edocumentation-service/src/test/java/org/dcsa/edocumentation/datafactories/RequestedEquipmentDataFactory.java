package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipment;

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
                    .weightUnit("KG")
                    .build()))
        .build();
  }
}
