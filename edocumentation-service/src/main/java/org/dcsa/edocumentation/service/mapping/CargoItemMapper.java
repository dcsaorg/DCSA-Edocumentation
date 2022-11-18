package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CargoItemMapper {

  CargoItem toDAO(CargoItemTO cargoItemTO);
}
