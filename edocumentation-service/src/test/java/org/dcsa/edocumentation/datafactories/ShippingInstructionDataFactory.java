package org.dcsa.edocumentation.datafactories;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;

@UtilityClass
public class ShippingInstructionDataFactory {

  public ShippingInstruction singleShallowShippingInstruction() {
    return ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECEIVED)
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .isShippedOnBoardType(true)
      .numberOfCopiesWithCharges(3)
      .numberOfOriginalsWithCharges(3)
      .isElectronic(false)
      .isToOrder(true)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt(DisplayedAddress.builder()
        .addressLine1("Amsterdam")
        .build())
      .displayedNameForPlaceOfDelivery(
        DisplayedAddress.builder()
          .addressLine1("Singapore")
          .build())
      .displayedNameForPortOfLoad(
        DisplayedAddress.builder()
          .addressLine1("Port of Rotterdam")
          .build())
      .displayedNameForPortOfDischarge(
        DisplayedAddress.builder()
          .addressLine1("Port of Singapore")
          .build())
      .build();
  }

  public ShippingInstruction singleShallowShippingInstructionWithPlaceOfIssueAndShipments() {
    Location placeOfIssue = Location.builder()
      .id(UUID.randomUUID())
      .address(Address.builder()
        .name("dummy address")
        .city("dummy street")
        .postCode("dummy postcode")
        .country("dummy country")
        .build())
      .build();

    Booking origBooking = BookingDataFactory.singleShallowBooking();
    var bookingData = origBooking.getBookingData().toBuilder()
      .termsAndConditions("dummy terms and conditions")
      .shipmentLocations(
        Set.of(
          ShipmentLocation.builder()
            .location(Location.builder().UNLocationCode("DKCPH").build())
            .shipmentLocationTypeCode(LocationType.POL.name())
            .build(),
          ShipmentLocation.builder()
            .location(Location.builder().UNLocationCode("DEHAM").build())
            .shipmentLocationTypeCode(LocationType.POD.name())
            .build()
        ))
      .shipmentTransports(Set.of(
        ShipmentTransport.builder()
          .plannedArrivalDate(LocalDate.of(2020, 1, 1))
          .plannedDepartureDate(LocalDate.of(2020, 1, 8))
          .loadLocation(Location.builder().UNLocationCode("NLRTM").build())
          .dischargeLocation(Location.builder().UNLocationCode("USMIA").build())
          .modeOfTransport(DCSATransportType.VESSEL.name())
          .vesselName("Emma Maersk")
          .vesselIMONumber("9321483")
          .build()
      ))
      .carrier(Carrier.builder().carrierName("dummy carrier").build())
      .build();
    var booking = origBooking.toBuilder()
      .bookingStatus("CONFIRMED")
      .bookingData(bookingData)
      .lastConfirmedBookingData(bookingData)
      .build();

    var consignmentItems = List.of(ConsignmentItem.builder()
      .booking(booking)
      .build());

    return ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECEIVED)
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .isShippedOnBoardType(true)
      .numberOfCopiesWithCharges(3)
      .numberOfOriginalsWithCharges(3)
      .isElectronic(false)
      .isToOrder(true)
      .placeOfIssue(placeOfIssue)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt(
        DisplayedAddress.builder()
          .addressLine1("Los Angeles")
          .build())
      .displayedNameForPlaceOfDelivery(
        DisplayedAddress.builder()
          .addressLine1("Hong Kong")
          .build())
      .displayedNameForPortOfLoad(
        DisplayedAddress.builder()
          .addressLine1("Port of Long Beach")
          .build())
      .displayedNameForPortOfDischarge(
        DisplayedAddress.builder()
          .addressLine1("Port of Hong Kong")
          .build())
      .consignmentItems(consignmentItems)
      .build();
  }

}
