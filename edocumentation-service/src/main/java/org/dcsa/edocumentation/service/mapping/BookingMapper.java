package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    uses = {
      LocationMapper.class,
      DisplayedAddressMapper.class,
      DocumentPartyMapper.class,
      RequestedEquipmentGroupMapper.class,
      CommodityMapper.class,
      RequestedChangeMapper.class,
      ReferenceMapper.class,
      ShipmentLocationMapper.class,
    }
)
public interface BookingMapper {

  @Mapping(target = "requestedEquipments", ignore = true)
  @Mapping(target = "shipmentLocations", ignore = true)
  @Mapping(target = "documentParties", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isNew", ignore = true)
  @Mapping(target = "validUntil", ignore = true)
  // To make it pick up the parameter
  @Mapping(source = "voyage", target = "voyage")
  // TODO: Align names between TO and DAO
  @Mapping(source = "bookingTO.placeOfBLIssue", target = "placeOfIssue")
  Booking toDAO(BookingTO bookingTO, Voyage voyage, Vessel vessel);

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "voyage.universalVoyageReference", target = "universalExportVoyageReference")
  @Mapping(source = "voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "voyage.service.carrierServiceCode", target = "carrierServiceCode")
  @Mapping(source = "voyage.service.universalServiceReference", target = "universalServiceReference")
  @Mapping(source = "vessel.vesselIMONumber", target = "vesselIMONumber")
  BookingTO toDTO(Booking booking);

  BookingRefStatusTO toStatusDTO(Booking booking);
}

