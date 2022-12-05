package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.EquipmentAssignment;
import org.dcsa.edocumentation.transferobjects.unofficial.EquipmentAssignmentTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EquipmentAssignmentMapper {

  EquipmentAssignment toDAO(EquipmentAssignmentTO equipmentAssignmentTO, List<Equipment> equipments);
}
