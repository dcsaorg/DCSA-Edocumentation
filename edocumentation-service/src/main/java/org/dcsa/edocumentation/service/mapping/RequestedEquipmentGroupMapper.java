package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestedEquipmentGroupMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(source = "requestedEquipment.isoEquipmentCode", target = "requestedEquipmentIsoEquipmentCode") //ToDo needs to be adjusted in DDT-1333
  RequestedEquipmentGroup toDAO(RequestedEquipmentTO requestedEquipment, Booking booking);

  // TODO figure this one out
  // @Mapping(
  //  source = "equipments",
  //  target = "equipmentReferences")
  @Mapping(source = "requestedEquipmentIsoEquipmentCode", target = "isoEquipmentCode")
  @Mapping(source = "activeReeferSettings", target = "activeReeferSettings", ignore = true) // TODO figure this one out
  RequestedEquipmentTO toTO(RequestedEquipmentGroup requestedEquipment);

  default String mapEquipmentToEquipmentReference(Equipment equipment) {
    return equipment.getEquipmentReference();
  }
}
