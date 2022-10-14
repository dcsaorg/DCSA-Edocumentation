package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Service;
import org.dcsa.edocumentation.transferobjects.ServiceTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
  ServiceTO toDTO(Service service);
}
