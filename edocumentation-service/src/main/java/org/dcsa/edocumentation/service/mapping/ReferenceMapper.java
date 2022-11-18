package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReferenceMapper {

  //TODO can be removed if using the same setup as Shipping insteruction
  @Mapping(source = "booking", target = "booking")
  Reference toDAO(ReferenceTO referenceTO, Booking booking);

  Reference toDAO(ReferenceTO referenceTO);
}
