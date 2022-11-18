package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CargoItemMapper {

  //Need to generate the ID, since it can happen that two cargoItems have the same values and therefor cannot be added to the set
  CargoItem toDAO(CargoItemTO cargoItemTO);
}
