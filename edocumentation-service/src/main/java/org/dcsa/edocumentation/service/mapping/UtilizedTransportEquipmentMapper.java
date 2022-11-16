package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilizedTransportEquipmentMapper {

  //ToDo inject CargoItem in here to create a stuffing service
  UtilizedTransportEquipment toDAO(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO);
}
