package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;

import java.util.stream.Stream;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface EquipmentMapper {
  Equipment toDAO(EquipmentTO equipmentTO);

  EquipmentTO toDTO(Equipment equipment);

  default Stream<EquipmentTO> toNonNullableDTOStream(RequestedEquipmentTO requestedEquipmentTO) {
    if (requestedEquipmentTO == null || requestedEquipmentTO.equipmentReferences() == null) {
      return Stream.empty();
    }
    return requestedEquipmentTO.equipmentReferences().stream()
      .map(reference -> EquipmentTO.builder()
        .equipmentReference(reference)
        .isoEquipmentCode(requestedEquipmentTO.isoEquipmentCode())
        .build());
  }
}
