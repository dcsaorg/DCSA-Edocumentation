package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {
      LocationMapper.class,
      DisplayedAddressMapper.class,
      RequestedEquipmentMapper.class
    }
)
public interface BookingMapper {
  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  Booking toDAO(BookingTO bookingTO);

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "vessel.vesselIMONumber", target = "vesselIMONumber")
  BookingTO toDTO(Booking booking);

  BookingRefStatusTO toStatusDTO(Booking booking);
}

