package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {LocationMapper.class, DocumentStatusMapper.class})
public interface TransportMapper {

  @Mapping(source = "transport.loadTransportCall.eventDateTimeDeparture", target = "plannedDepartureDate")
  @Mapping(source = "transport.dischargeTransportCall.eventDateTimeArrival", target = "plannedArrivalDate")
  @Mapping(source = "transport.dischargeTransportCall.vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(source = "transport.dischargeTransportCall.vessel.name", target = "vesselName")
  @Mapping(
      source = "transport.dischargeTransportCall.importVoyage.carrierVoyageNumber",
      target = "carrierImportVoyageNumber")
  @Mapping(
    source = "transport.dischargeTransportCall.importVoyage.universalVoyageReference",
    target = "universalImportVoyageReference")
  @Mapping(
      source = "transport.dischargeTransportCall.exportVoyage.carrierVoyageNumber",
      target = "carrierExportVoyageNumber")
  @Mapping(
    source = "transport.dischargeTransportCall.exportVoyage.universalVoyageReference",
    target = "universalExportVoyageReference")
  @Mapping(
    source = "transport.loadTransportCall.location",
    target = "loadLocation")
  @Mapping(
    source = "transport.dischargeTransportCall.location",
    target = "dischargeLocation")
  TransportTO shipmentTransportToTransportTO(ShipmentTransport shipmentTransport);
}
