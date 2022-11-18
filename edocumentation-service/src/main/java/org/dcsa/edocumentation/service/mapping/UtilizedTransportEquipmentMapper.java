package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UtilizedTransportEquipmentMapper {

  UtilizedTransportEquipment toDAO(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO);
}
