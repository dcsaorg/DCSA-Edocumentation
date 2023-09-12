package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    unmappedTargetPolicy = ReportingPolicy.WARN,  // FIXME: Remove this when we are ready to do ERROR level reporting
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

  @Mapping(source = "commodities", target = "commodities", ignore = true)
  @Mapping(target = "shipmentLocations", ignore = true)
  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  Booking toDAO(BookingTO bookingTO);

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "voyage.universalVoyageReference", target = "universalExportVoyageReference")
  @Mapping(source = "voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "voyage.service.carrierServiceCode", target = "carrierServiceCode")
  @Mapping(source = "voyage.service.universalServiceReference", target = "universalServiceReference")
  @Mapping(source = "vessel.vesselIMONumber", target = "vesselIMONumber")
  BookingTO toDTO(Booking booking);

  BookingRefStatusTO toStatusDTO(Booking booking);
}

