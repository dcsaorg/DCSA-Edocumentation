package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
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
    return List.of(requestedEquipmentTORef1(), requestedEquipmentTORef2());
  }

  public static RequestedEquipmentTO requestedEquipmentTORef1() {
    return RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("Equipment_Ref_01"))
      .isoEquipmentCode("GP22")
      .units(1)
      .isShipperOwned(true)
      .activeReeferSettings(BookingActiveReeferSettingsDataFactory.bkgFreezer())
      .build();
  }

  public static RequestedEquipmentTO requestedEquipmentTORef2() {
    return RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("Equipment_Ref_02"))
      .isoEquipmentCode("GP22")
      .units(1)
      .isShipperOwned(false)
      .build();
  }

  public static List<RequestedEquipmentGroup> requestedEquipmentList() {
    return List.of(requestedEquipmentRef1(), requestedEquipmentRef2());
  }

  public static RequestedEquipmentGroup requestedEquipmentRef1() {
    return RequestedEquipmentGroup.builder()
      .isShipperOwned(true)
      .requestedEquipmentIsoEquipmentCode("GP22")
      .requestedEquipmentUnits(1)
      .build();
  }

  public static RequestedEquipmentGroup requestedEquipmentRef2() {
    return RequestedEquipmentGroup.builder()
      .isShipperOwned(false)
      .requestedEquipmentIsoEquipmentCode("GP22")
      .requestedEquipmentUnits(1)
      .build();
  }
}
