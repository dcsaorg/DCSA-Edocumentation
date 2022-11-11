package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
  AddressTO toDTO(Address address);
  Address toDAO(AddressTO address);
}
