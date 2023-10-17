package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedBooking;
import org.dcsa.edocumentation.transferobjects.unofficial.ConfirmedBookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ConfirmedBookingTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  unmappedTargetPolicy = ReportingPolicy.WARN,  // FIXME; Remove this line when we can migrate to ERROR
  config = EDocumentationMappingConfig.class,
  uses = {
    LocationMapper.class,
    BookingMapper.class,
    TransportMapper.class,
    ConfirmedEquipmentMapper.class,
    ShipmentCutOffTimeMapper.class,
    AdvanceManifestFilingMapper.class
  })
public interface ConfirmedBookingMapper {
  @Mapping(source = "shipmentTransports", target = "transports")
  ConfirmedBookingTO confirmedBookingToConfirmedBookingTO(ConfirmedBooking confirmedBooking);

  @Mapping(source = "confirmedBooking.booking.bookingRequestUpdatedDateTime", target = "bookingRequestUpdatedDateTime")
  @Mapping(source = "confirmedBooking.booking.bookingRequestCreatedDateTime", target = "bookingRequestCreatedDateTime")
  ConfirmedBookingRefStatusTO toStatusDTO(ConfirmedBooking confirmedBooking, String bookingStatus);
}
