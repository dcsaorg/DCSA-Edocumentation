package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.entity.Location;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class ShippingInstructionDataFactory {

  public ShippingInstruction singleShallowShippingInstruction() {
    return ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECE)
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

    Shipment shipments =
        Shipment.builder()
            .carrierBookingReference(UUID.randomUUID().toString())
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .shipmentCreatedDateTime(OffsetDateTime.now())
            .termsAndConditions("dummy terms and conditions")
            .booking(BookingDataFactory.singleShallowBooking())
          .shipmentLocations(
            Set.of(
              ShipmentLocation.builder()
                .location(Location.builder().UNLocationCode("DKCPH").build())
                .shipmentLocationTypeCode(LocationType.POL)
                .build(),
              ShipmentLocation.builder()
                .location(Location.builder().UNLocationCode("DEHAM").build())
                .shipmentLocationTypeCode(LocationType.POD)
                .build()
            ))
            .shipmentTransports(Set.of(
              ShipmentTransport.builder()
                .transport(Transport.builder()
                  .loadTransportCall(TransportCall.builder()
                    .eventDateTimeDeparture(OffsetDateTime.of(2020, 1, 1, 1, 1, 1, 0, ZoneOffset.UTC))
                    .location(Location.builder().UNLocationCode("NLRTM").build())
                    .build())
                  .dischargeTransportCall(TransportCall.builder()
                    .eventDateTimeArrival(OffsetDateTime.of(2020, 1, 8, 1, 1, 1, 0, ZoneOffset.UTC))
                    .location(Location.builder().UNLocationCode("USMIA").build())
                    .build())
                  .build())
                .build()
            ))
            .carrier(Carrier.builder().carrierName("dummy carrier").build())
            .build();

    Set<ConsignmentItem> consignmentItems = Set.of(ConsignmentItem.builder()
      .shipment(shipments)
      .build());

    return ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECE)
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

  public List<ShippingInstruction> multipleShallowShippingInstructionWithPlaceOfIssueAndShipments() {
    Location placeOfIssue = Location.builder()
      .id(UUID.randomUUID())
      .address(Address.builder()
        .name("dummy address")
        .city("dummy street")
        .postCode("dummy postcode")
        .country("dummy country")
        .build())
      .build();

    Shipment shipment = Shipment.builder()
      .carrierBookingReference(UUID.randomUUID().toString())
      .shipmentCreatedDateTime(OffsetDateTime.now())
      .shipmentCreatedDateTime(OffsetDateTime.now())
      .termsAndConditions("dummy terms and conditions")
      .booking(BookingDataFactory.singleShallowBooking())
      .carrier(Carrier.builder()
        .carrierName("dummy carrier")
        .build())
      .build();

    Set<ConsignmentItem> consignmentItems = Set.of(
      ConsignmentItem.builder()
        .shipment(shipment)
        .build()
    );

    return List.of(ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECE)
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .isShippedOnBoardType(true)
      .isElectronic(true)
      .isToOrder(false)
      .placeOfIssue(placeOfIssue)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt(
        DisplayedAddress.builder()
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
      .consignmentItems(consignmentItems)
      .build(), singleShallowShippingInstructionWithPlaceOfIssueAndShipments());
  }
}
