package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    CustomsReferenceMapper.class
  }
)
public interface CargoItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "utilizedTransportEquipment", ignore = true)
  CargoItem toDAO(CargoItemTO cargoItemTO);

  CargoItemTO toDTO(CargoItem cargoItem);
}
