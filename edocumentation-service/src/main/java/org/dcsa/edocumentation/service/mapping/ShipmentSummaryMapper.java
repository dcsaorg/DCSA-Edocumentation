package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ShipmentSummaryMapper {
  @Mapping(
      source = "booking.carrierBookingRequestReference",
      target = "carrierBookingRequestReference")
  @Mapping(source = "booking.documentStatus", target = "documentStatus")
  ShipmentSummaryTO shipmentToShipmentSummary(Shipment shipment);
}
