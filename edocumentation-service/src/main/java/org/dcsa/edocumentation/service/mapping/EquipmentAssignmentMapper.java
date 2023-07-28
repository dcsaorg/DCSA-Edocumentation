package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.EquipmentAssignment;
import org.dcsa.edocumentation.transferobjects.unofficial.EquipmentAssignmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface EquipmentAssignmentMapper {

  @Mapping(target = "ipments", ignore = true)  // FIXME: I do not know why the mapper "sees" an "ipments" attribute.
  EquipmentAssignment toDAO(EquipmentAssignmentTO equipmentAssignmentTO, List<Equipment> equipments);
}
