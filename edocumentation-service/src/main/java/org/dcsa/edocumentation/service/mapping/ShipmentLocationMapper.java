package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedBooking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = LocationMapper.class)
public interface ShipmentLocationMapper {

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  @Mapping(target = "confirmedBooking", ignore = true)
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, Booking booking);

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  // Otherwise, it pulls the booking out of the confirmedBooking and throws away the confirmedBooking
  @Mapping(target = "booking", ignore = true)
  @Mapping(source = "confirmedBooking", target = "confirmedBooking")
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, ConfirmedBooking confirmedBooking);

  ShipmentLocationTO toDTO(ShipmentLocation shipmentLocation);
}
