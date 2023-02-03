package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedService;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ValueAddedServiceRequestMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  ValueAddedService toDAO(ValueAddedServiceTO request, Booking booking);
}
