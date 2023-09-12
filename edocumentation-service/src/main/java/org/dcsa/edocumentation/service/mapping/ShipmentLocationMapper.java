package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = LocationMapper.class)
public interface ShipmentLocationMapper {

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  @Mapping(target = "shipment", ignore = true)
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, Booking booking);

  @Mapping(target = "id", ignore = true) /* It defaults to pulling booking.getId(), which is wrong */
  // Otherwise, it pulls the booking out of the shipment and throws away the shipment
  @Mapping(target = "booking", ignore = true)
  @Mapping(source = "shipment", target = "shipment")
  ShipmentLocation toDAO(ShipmentLocationTO shipmentLocationTO, Shipment shipment);

  ShipmentLocationTO toDTO(ShipmentLocation shipmentLocation);
}
