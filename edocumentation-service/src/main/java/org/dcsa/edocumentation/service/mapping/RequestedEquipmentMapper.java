package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipment;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestedEquipmentMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  RequestedEquipment toDAO(RequestedEquipmentTO requestedEquipment, Booking booking);

  @Mapping(
    source = "equipments",
    target = "equipmentReferences")
  RequestedEquipmentTO toTO(RequestedEquipment requestedEquipment);

  default String mapEquipmentToEquipmentReference(Equipment equipment) {
    return equipment.getEquipmentReference();
  }
}
