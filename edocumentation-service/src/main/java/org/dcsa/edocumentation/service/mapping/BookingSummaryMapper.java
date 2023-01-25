package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingSummaryMapper {
  BookingSummaryTO BookingToBookingSummary(BookingTO bookingTO);
}
