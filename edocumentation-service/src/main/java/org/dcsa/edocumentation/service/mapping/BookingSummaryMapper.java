package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BookingSummaryMapper {

  @Mapping(source = "booking.vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(source = "preCarriageModeOfTransportCode", target = "preCarriageModeOfTransportCode", qualifiedByName = "toDCSATransportType")
  BookingSummaryTO BookingToBookingSummary(Booking booking);

  @Named("toDCSATransportType")
  static DCSATransportType toDcsaTransportType(String modeOfTransportCode) {

    if (modeOfTransportCode == null) {
      return null;
    }

    return switch (modeOfTransportCode) {
      case "1" -> DCSATransportType.VESSEL;
      case "2" -> DCSATransportType.RAIL;
      case "3" -> DCSATransportType.TRUCK;
      case "8" -> DCSATransportType.BARGE;
      default -> throw ConcreteRequestErrorMessageException.internalServerError("Could not convert TransportTypeCode to DCSA TransportType");

    };
  }
}
