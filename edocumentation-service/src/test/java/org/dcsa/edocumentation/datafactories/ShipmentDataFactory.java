package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ShipmentDataFactory {

  public Shipment singleShipmentWithoutBooking() {
    return Shipment.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
        .carrierBookingReference("carrierBookingReference")
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentUpdatedDateTime(OffsetDateTime.now())
        .termsAndConditions("TERMS AND CONDITIONS")
        .build();
  }

  public Shipment singleShipmentWithBooking() {
    Booking booking = BookingDataFactory.singleShallowBooking();
    return Shipment.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
        .carrierBookingReference("carrierBookingReference")
        .booking(booking)
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentUpdatedDateTime(OffsetDateTime.now())
        .termsAndConditions("TERMS AND CONDITIONS")
        .build();
  }

  public List<Shipment> multipleShipmentsWithBooking() {
    List<Booking> booking =
        BookingDataFactory.multipleShallowBookingsWithVesselAndModeOfTransport();
    Shipment shipment1 =
        Shipment.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
            .carrierBookingReference("carrierBookingReference")
            .booking(booking.get(0))
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .shipmentUpdatedDateTime(OffsetDateTime.now())
            .termsAndConditions("TERMS AND CONDITIONS")
            .build();

    Shipment shipment2 =
        Shipment.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5f2"))
            .carrierBookingReference("carrierBookingReference")
            .booking(booking.get(1))
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .shipmentUpdatedDateTime(OffsetDateTime.now())
            .termsAndConditions("TERMS AND CONDITIONS")
            .build();

    return List.of(shipment1, shipment2);
  }
}
