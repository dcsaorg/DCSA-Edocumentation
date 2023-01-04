package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
      LocationMapper.class,
      DisplayedAddressMapper.class,
      DocumentPartyMapper.class,
      RequestedEquipmentGroupMapper.class,
      CommodityMapper.class
    }
)
public interface BookingMapper {

  @Mapping(source = "commodities", target = "commodities", ignore = true)
  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  Booking toDAO(BookingTO bookingTO);

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "voyage.universalVoyageReference", target = "universalExportVoyageReference")
  @Mapping(source = "voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "voyage.service.carrierServiceCode", target = "carrierServiceCode")
  @Mapping(source = "voyage.service.universalServiceReference", target = "universalServiceReference")
  @Mapping(source = "vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(source = "modeOfTransport.dcsaTransportType", target = "preCarriageModeOfTransportCode")
  BookingTO toDTO(Booking booking);

  BookingRefStatusTO toStatusDTO(Booking booking);
}

