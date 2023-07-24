package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentTransportMapper {

  // Required to make the mapper use the argument rather than throwing it away
  @Mapping(source = "shipment", target = "shipment")
  @Mapping(source = "loadLocation", target = "loadLocation")
  @Mapping(source = "dischargeLocation", target = "dischargeLocation")
  @Mapping(source = "shipmentTransportTO.transportPlanStage", target = "transportPlanStageCode")
  @Mapping(target = "id", ignore = true)
  ShipmentTransport toDAO(TransportTO shipmentTransportTO,
                          Shipment shipment,
                          Location loadLocation,
                          Location dischargeLocation
                          );
}
