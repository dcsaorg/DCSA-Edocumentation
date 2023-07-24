package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModeOfTransportMapper {
  DCSATransportType toDAO(org.dcsa.edocumentation.transferobjects.enums.DCSATransportType transportType);
  org.dcsa.edocumentation.transferobjects.enums.DCSATransportType toTO(DCSATransportType transportType);
}
