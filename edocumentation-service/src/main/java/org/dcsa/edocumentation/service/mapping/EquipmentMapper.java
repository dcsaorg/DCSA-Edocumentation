package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

  Equipment toDAO(EquipmentTO equipmentTO);

  EquipmentTO toTo(Equipment equipment);
}
