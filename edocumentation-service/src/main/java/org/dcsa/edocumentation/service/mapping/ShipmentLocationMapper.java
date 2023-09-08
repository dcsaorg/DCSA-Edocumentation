package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Location;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = LocationMapper.class)
public interface ShipmentLocationMapper {

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  // Needed to force the argument rather than shipmentLocationTO.location()
  @Mapping(source = "location", target = "location")
  @Mapping(target = "shipment", ignore = true)
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, Location location, Booking booking);

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  // Needed to force the argument rather than shipmentLocationTO.location()
  @Mapping(source = "location", target = "location")
  // Otherwise, it pulls the booking out of the shipment and throws away the shipment
  @Mapping(source = "shipment", target = "shipment")
  @Mapping(target = "booking", ignore = true)
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, Location location, Shipment shipment);

  ShipmentLocationTO toDTO(ShipmentLocation shipmentLocation);
}
