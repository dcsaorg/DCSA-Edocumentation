package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
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

  @Mapping(source = "shipment.booking.bookingRequestUpdatedDateTime", target = "bookingRequestUpdatedDateTime")
  @Mapping(source = "shipment.booking.bookingRequestCreatedDateTime", target = "bookingRequestCreatedDateTime")
  ShipmentRefStatusTO toStatusDTO(Shipment shipment, BkgDocumentStatus documentStatus);
}
