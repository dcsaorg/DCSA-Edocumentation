package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentSummaryMapper {
  @Mapping(
      source = "booking.carrierBookingRequestReference",
      target = "carrierBookingRequestReference")
  ShipmentSummaryTO ShipmentToShipmentSummary(Shipment shipment);
}
