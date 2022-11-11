package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.domain.persistence.entity.Location;

import java.time.OffsetDateTime;
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
      .isShippedOnboardType(true)
      .numberOfCopiesWithCharges(3)
      .numberOfOriginalsWithCharges(3)
      .isElectronic(false)
      .isToOrder(true)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt("Amsterdam")
      .displayedNameForPlaceOfDelivery("Singapore")
      .displayedNameForPortOfLoad("Port of Rotterdam")
      .displayedNameForPortOfDischarge("Port of Singapore")
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

    Set<Shipment> shipments = Set.of(Shipment.builder()
        .carrierBookingReference(UUID.randomUUID().toString())
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .termsAndConditions("dummy terms and conditions")
        .booking(BookingDataFactory.singleShallowBooking())
        .carrier(Carrier.builder()
          .carrierName("dummy carrier")
          .build())
      .build());

    return ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECE)
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .isShippedOnboardType(true)
      .numberOfCopiesWithCharges(3)
      .numberOfOriginalsWithCharges(3)
      .isElectronic(false)
      .isToOrder(true)
      .placeOfIssueID(placeOfIssue)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt("Los Angeles")
      .displayedNameForPlaceOfDelivery("Hong Kong")
      .displayedNameForPortOfLoad("Port of Long Beach")
      .displayedNameForPortOfDischarge("Port of Hong Kong")
      .shipments(shipments)
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

    Set<Shipment> shipments = Set.of(Shipment.builder()
      .carrierBookingReference(UUID.randomUUID().toString())
      .shipmentCreatedDateTime(OffsetDateTime.now())
      .shipmentCreatedDateTime(OffsetDateTime.now())
      .termsAndConditions("dummy terms and conditions")
      .booking(BookingDataFactory.singleShallowBooking())
      .carrier(Carrier.builder()
        .carrierName("dummy carrier")
        .build())
      .build());

    return List.of(ShippingInstruction.builder()
      .id(UUID.randomUUID())
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECE)
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .isShippedOnboardType(true)
      .isElectronic(true)
      .isToOrder(false)
      .placeOfIssueID(placeOfIssue)
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .displayedNameForPlaceOfReceipt("Amsterdam")
      .displayedNameForPlaceOfDelivery("Singapore")
      .displayedNameForPortOfLoad("Port of Rotterdam")
      .displayedNameForPortOfDischarge("Port of Singapore")
      .shipments(shipments)
      .build(), singleShallowShippingInstructionWithPlaceOfIssueAndShipments());
  }
}
