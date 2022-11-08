package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {LocationMapper.class, DisplayedAddressMapper.class})
public interface BookingMapper {

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  BookingTO bookingToBookingTO(Booking booking);
}
