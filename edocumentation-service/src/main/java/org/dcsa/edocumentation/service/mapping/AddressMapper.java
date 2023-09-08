package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Address;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface AddressMapper {
  AddressTO toDTO(Address address);
  @Mapping(target = "id", ignore = true)
  Address toDAO(AddressTO address);
}
