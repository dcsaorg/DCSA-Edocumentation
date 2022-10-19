package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;

import java.time.OffsetDateTime;
import java.util.List;
@UtilityClass
public class ShipmentSummaryDataFactory {

  public ShipmentSummaryTO singleShipmentSummaryTO() {
   return ShipmentSummaryTO.builder()
     .shipmentCreatedDateTime(OffsetDateTime.now())
     .shipmentUpdatedDateTime(OffsetDateTime.now())
     .carrierBookingReference("BOOKING_REF_01")
     .documentStatus(BkgDocumentStatus.RECE)
     .carrierBookingRequestReference("BOOKING_REQ_REF_01")
     .termsAndConditions("TERMS AND CONDITIONS")
     .build();
  }

  public List<ShipmentSummaryTO> multipleShipmentSummaryTO() {
    ShipmentSummaryTO shipmentSummaryTO1 =
      ShipmentSummaryTO.builder()
      .shipmentCreatedDateTime(OffsetDateTime.now())
      .shipmentUpdatedDateTime(OffsetDateTime.now())
      .carrierBookingReference("BOOKING_REF_01")
      .documentStatus(BkgDocumentStatus.RECE)
      .carrierBookingRequestReference("BOOKING_REQ_REF_01")
      .termsAndConditions("TERMS AND CONDITIONS")
      .build();

    ShipmentSummaryTO shipmentSummaryTO2 =
      ShipmentSummaryTO.builder()
        .shipmentCreatedDateTime(OffsetDateTime.now())
        .shipmentUpdatedDateTime(OffsetDateTime.now())
        .carrierBookingReference("BOOKING_REF_02")
        .documentStatus(BkgDocumentStatus.RECE)
        .carrierBookingRequestReference("BOOKING_REQ_REF_02")
        .termsAndConditions("TERMS AND CONDITIONS")
        .build();

  return List.of(shipmentSummaryTO1,shipmentSummaryTO2);
  }
}
