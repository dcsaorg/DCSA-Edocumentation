package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Location;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = AddressMapper.class)
public interface LocationMapper {
  LocationTO toDTO(Location location);
  @Mapping(target = "id", ignore = true)
  Location toDAO(LocationTO locationTO);
}
