package org.dcsa.edocumentation.domain.persistence.entity;

import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingInstructionTest {

  @Test
  void shippingInstructionTest_hasUnconfirmedBookings() {

    Shipment unconfirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(Booking.builder().documentStatus(BkgDocumentStatus.PENC).build())
            .build();

    Shipment confirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(Booking.builder().documentStatus(BkgDocumentStatus.CONF).build())
            .build();

    Set<ConsignmentItem> consignmentItems =
        Set.of(
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(unconfirmedShipment).build(),
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(confirmedShipment).build());

    ShippingInstruction si =
        ShippingInstruction.builder().consignmentItems(consignmentItems).build();

    Boolean hasOnlyConfirmedBookings = si.hasOnlyConfirmedBookings();

    assertFalse(hasOnlyConfirmedBookings);
  }

  @Test
  void shippingInstructionTest_hasConfirmedBookings() {

    Shipment unconfirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(Booking.builder().documentStatus(BkgDocumentStatus.CONF).build())
            .build();

    Shipment confirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(Booking.builder().documentStatus(BkgDocumentStatus.CONF).build())
            .build();

    Set<ConsignmentItem> consignmentItems =
        Set.of(
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(unconfirmedShipment).build(),
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(confirmedShipment).build());

    ShippingInstruction si =
        ShippingInstruction.builder().consignmentItems(consignmentItems).build();

    Boolean hasOnlyConfirmedBookings = si.hasOnlyConfirmedBookings();

    assertTrue(hasOnlyConfirmedBookings);
  }

  @Test
  void shippingInstructionTest_hasOneBooking() {

    Shipment unconfirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(
                Booking.builder()
                    .carrierBookingRequestReference("Ref1")
                    .documentStatus(BkgDocumentStatus.CONF)
                    .build())
            .build();

    Shipment confirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(
                Booking.builder()
                    .carrierBookingRequestReference("Ref1")
                    .documentStatus(BkgDocumentStatus.CONF)
                    .build())
            .build();

    Set<ConsignmentItem> consignmentItems =
        Set.of(
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(unconfirmedShipment).build(),
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(confirmedShipment).build());

    ShippingInstruction si =
        ShippingInstruction.builder().consignmentItems(consignmentItems).build();

    Boolean hasOneBooking = si.containsOneBooking();

    assertTrue(hasOneBooking);
  }

  @Test
  void shippingInstructionTest_hasMultipleBookings() {

    Shipment unconfirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(
                Booking.builder()
                    .carrierBookingRequestReference("Ref1")
                    .documentStatus(BkgDocumentStatus.CONF)
                    .build())
            .build();

    Shipment confirmedShipment =
        Shipment.builder()
            .id(UUID.randomUUID())
            .booking(
                Booking.builder()
                    .carrierBookingRequestReference("Ref2")
                    .documentStatus(BkgDocumentStatus.CONF)
                    .build())
            .build();

    Set<ConsignmentItem> consignmentItems =
        Set.of(
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(unconfirmedShipment).build(),
            ConsignmentItem.builder().id(UUID.randomUUID()).shipment(confirmedShipment).build());

    ShippingInstruction si =
        ShippingInstruction.builder().consignmentItems(consignmentItems).build();

    Boolean hasOneBooking = si.containsOneBooking();

    assertFalse(hasOneBooking);
  }
}
