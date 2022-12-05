package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UtilizedTransportEquipmentMapper {

  @Mapping(source = "utilizedTransportEquipmentTO.isShipperOwned", target = "isShipperOwned")
  @Mapping(source = "requestedEquipmentGroup", target = "requestedEquipmentGroup")
  @Mapping(source = "requestedEquipmentGroup.id", target = "id", ignore = true)
  UtilizedTransportEquipment toDAO(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO, RequestedEquipmentGroup requestedEquipmentGroup);
}
