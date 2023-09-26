package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShipmentTO;
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
    DocumentStatusMapper.class,
    BookingMapper.class,
    TransportMapper.class,
    ConfirmedEquipmentMapper.class,
    ShipmentCutOffTimeMapper.class,
    AdvanceManifestFilingMapper.class
  })
public interface ShipmentMapper {
  @Mapping(source = "shipmentTransports", target = "transports")
  ShipmentTO shipmentToShipmentTO(Shipment shipment);

  @Mapping(source = "shipment.booking.bookingRequestUpdatedDateTime", target = "bookingRequestUpdatedDateTime")
  @Mapping(source = "shipment.booking.bookingRequestCreatedDateTime", target = "bookingRequestCreatedDateTime")
  ShipmentRefStatusTO toStatusDTO(Shipment shipment, BkgDocumentStatus documentStatus);
}
