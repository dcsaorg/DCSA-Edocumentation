package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class, uses = LocationMapper.class)
public interface ShipmentTransportMapper {

  // Required to make the mapper use the argument rather than throwing it away
  @Mapping(source = "bookingData", target = "bookingData")
  @Mapping(source = "loadLocation", target = "loadLocation")
  @Mapping(source = "dischargeLocation", target = "dischargeLocation")
  @Mapping(source = "shipmentTransportTO.transportPlanStage", target = "transportPlanStageCode")
  @Mapping(target = "id", ignore = true)
  ShipmentTransport toDAO(TransportTO shipmentTransportTO,
                          BookingData bookingData,
                          LocationTO loadLocation,
                          LocationTO dischargeLocation
                          );
}
