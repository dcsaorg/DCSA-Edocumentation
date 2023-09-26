package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class RequestedEquipmentDataFactory {

  public static RequestedEquipmentGroup singleRequestedEquipment() {
    return RequestedEquipmentGroup.builder()
        .id(UUID.randomUUID())
        .isShipperOwned(true)
        .isoEquipmentCode("size Type")
        .units(1)
        .build();
  }

  public static List<RequestedEquipmentTO> requestedEquipmentTOList() {
    return List.of(requestedEquipmentTORef1(), requestedEquipmentTORef2());
  }

  public static RequestedEquipmentTO requestedEquipmentTORef1() {
    return RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("Equipment_Ref_01"))
      .isoEquipmentCode("GP22")
      .units(1)
      .isShipperOwned(true)
      .activeReeferSettings(ActiveReeferSettingsDataFactory.activeReeferSettingsTO())
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
      .isoEquipmentCode("GP22")
      .units(1)
      .build();
  }

  public static RequestedEquipmentGroup requestedEquipmentRef2() {
    return RequestedEquipmentGroup.builder()
      .isShipperOwned(false)
      .isoEquipmentCode("GP22")
      .units(1)
      .build();
  }
}
