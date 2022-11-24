package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.transferobjects.ShipmentTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  injectionStrategy = InjectionStrategy.CONSTRUCTOR,
  uses = {LocationMapper.class,DocumentStatusMapper.class, BookingMapper.class, TransportMapper.class})
public interface ShipmentMapper {
  @Mapping(source = "shipmentTransports", target = "transports")
   ShipmentTO shipmentToShipmentTO(Shipment shipment);
}
