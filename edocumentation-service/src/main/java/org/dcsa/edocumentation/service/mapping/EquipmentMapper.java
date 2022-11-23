package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
  Equipment toDAO(EquipmentTO equipmentTO);

  default EquipmentTO toDTO(String reference) {
    return reference == null ? null : EquipmentTO.builder().equipmentReference(reference).build();
  }

  default Stream<EquipmentTO> toNonNullableDTOStream(List<EquipmentTO> equipments) {
    return equipments == null ? Stream.empty() : equipments.stream();
  }
}
