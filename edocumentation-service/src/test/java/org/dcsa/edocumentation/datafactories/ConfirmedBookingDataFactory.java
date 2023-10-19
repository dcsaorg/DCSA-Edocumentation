package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedBooking;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ConfirmedBookingDataFactory {

  public ConfirmedBooking singleConfirmedBookingWithoutBooking() {
    return ConfirmedBooking.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
        .carrierBookingReference("carrierBookingReference")
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentUpdatedDateTime(OffsetDateTime.now())
        .termsAndConditions("TERMS AND CONDITIONS")
        .build();
  }

  public ConfirmedBooking singleConfirmedBookingWithBooking() {
    BookingRequest bookingRequest = BookingDataFactory.singleShallowBooking();
    return ConfirmedBooking.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
        .carrierBookingReference("carrierBookingReference")
        .booking(bookingRequest)
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentUpdatedDateTime(OffsetDateTime.now())
        .termsAndConditions("TERMS AND CONDITIONS")
        .build();
  }

  public List<ConfirmedBooking> multipleConfirmedBookingsWithBooking() {
    List<BookingRequest> bookingRequest =
        BookingDataFactory.multipleShallowBookingsWithVesselAndModeOfTransport();
    ConfirmedBooking confirmedBooking1 =
        ConfirmedBooking.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
            .carrierBookingReference("carrierBookingReference")
            .booking(bookingRequest.get(0))
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .shipmentUpdatedDateTime(OffsetDateTime.now())
            .termsAndConditions("TERMS AND CONDITIONS")
            .build();

    ConfirmedBooking confirmedBooking2 =
        ConfirmedBooking.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
            .carrierBookingReference("carrierBookingReference")
            .booking(bookingRequest.get(1))
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .shipmentUpdatedDateTime(OffsetDateTime.now())
            .termsAndConditions("TERMS AND CONDITIONS")
            .build();

    return List.of(confirmedBooking1, confirmedBooking2);
  }
}
