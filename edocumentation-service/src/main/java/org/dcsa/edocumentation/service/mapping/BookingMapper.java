package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
  Booking toDAO(BookingTO bookingTO);

  BookingTO toDTO(Booking booking);

  BookingRefStatusTO toStatusDTO(Booking booking);
}
