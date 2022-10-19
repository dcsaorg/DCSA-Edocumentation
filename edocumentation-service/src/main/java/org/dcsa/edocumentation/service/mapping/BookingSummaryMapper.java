package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingSummaryMapper {

  @Mapping(source = "booking.vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(source = "updatedDateTime", target = "bookingRequestUpdatedDateTime")
  @Mapping(source = "bookingRequestDateTime", target = "bookingRequestCreatedDateTime")
  @Mapping(
      source = " booking.modeOfTransport.dcsaTransportType",
      target = "preCarriageModeOfTransportCode")
  BookingSummaryTO BookingToBookingSummary(Booking booking);
}
