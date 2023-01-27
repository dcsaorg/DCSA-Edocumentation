package org.dcsa.edocumentation.service.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        uses = {
                LocationMapper.class,
                DisplayedAddressMapper.class,
                RequestedEquipmentGroupMapper.class
        }
)
public abstract class BookingMapper {
  @Autowired
  private ObjectMapper objectMapper;

  public abstract BookingRefStatusTO toStatusDTO(BookingTO bookingTO);
  public abstract org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus toDAO(BkgDocumentStatus bkgDocumentStatus);

  @SneakyThrows
  public Booking toDAO(BookingTO bookingTO) {
    return Booking.builder()
            .carrierBookingRequestReference(bookingTO.carrierBookingRequestReference())
            .documentStatus(toDAO(bookingTO.documentStatus()))
            .content(objectMapper.writeValueAsString(bookingTO))
            .bookingRequestCreatedDateTime(bookingTO.bookingRequestCreatedDateTime())
            .bookingRequestUpdatedDateTime(bookingTO.bookingRequestUpdatedDateTime())
            .build();
  }

  @SneakyThrows
  public BookingTO toDTO(Booking booking) {
    return objectMapper.readValue(booking.getContent(), BookingTO.class);
  }

  @Mapping(source = "placeOfIssue", target = "placeOfBLIssue")
  @Mapping(source = "voyage.universalVoyageReference", target = "universalExportVoyageReference")
  @Mapping(source = "voyage.carrierVoyageNumber", target = "carrierExportVoyageNumber")
  @Mapping(source = "voyage.service.carrierServiceCode", target = "carrierServiceCode")
  @Mapping(source = "voyage.service.universalServiceReference", target = "universalServiceReference")
  @Mapping(source = "vessel.vesselIMONumber", target = "vesselIMONumber")
  @Mapping(source = "modeOfTransport.dcsaTransportType", target = "preCarriageModeOfTransportCode")
  @Mapping(source = "valueAddedServiceRequests", target = "valueAddedServices")
  public abstract BookingTO toDTO(org.dcsa.edocumentation.domain.persistence.entity.Booking booking);
}
