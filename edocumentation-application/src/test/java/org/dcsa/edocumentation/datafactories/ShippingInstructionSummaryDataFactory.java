package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class ShippingInstructionSummaryDataFactory {

  public ShippingInstructionSummaryTO singleShippingInstructionSummaryTO() {
    return ShippingInstructionSummaryTO.builder()
        .shippingInstructionReference(UUID.randomUUID().toString())
        .documentStatus(EblDocumentStatus.RECEIVED)
        .carrierBookingReferences(
            List.of("bca68f1d3b804ff88aaa1e43055432f7", "832deb4bd4ea4b728430b857c59bd057"))
        .shippingInstructionCreatedDateTime(OffsetDateTime.now())
        .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
        .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
        .isShippedOnBoardType(true)
        .isElectronic(true)
        .isToOrder(true)
        .areChargesDisplayedOnOriginals(false)
        .areChargesDisplayedOnOriginals(false)
        .displayedNameForPlaceOfDelivery(List.of("Amsterdam"))
        .displayedNameForPlaceOfReceipt(List.of("Singapore"))
        .displayedNameForPortOfLoad(List.of("Port of Singapore"))
        .displayedNameForPortOfDischarge(List.of("Port of Rotterdam"))
        .build();
  }

  public List<ShippingInstructionSummaryTO> multipleShippingInstructionSummaryTO() {
    ShippingInstructionSummaryTO shippingInstructionSummaryTO = ShippingInstructionSummaryTO.builder()
      .shippingInstructionReference(UUID.randomUUID().toString())
      .documentStatus(EblDocumentStatus.RECEIVED)
      .carrierBookingReferences(
        List.of("bca68f1d3b804ff88aaa1e43055432f7", "832deb4bd4ea4b728430b857c59bd057"))
      .shippingInstructionCreatedDateTime(OffsetDateTime.now())
      .shippingInstructionUpdatedDateTime(OffsetDateTime.now())
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .isShippedOnBoardType(true)
      .isElectronic(true)
      .isToOrder(true)
      .areChargesDisplayedOnOriginals(false)
      .areChargesDisplayedOnOriginals(false)
      .displayedNameForPlaceOfDelivery(List.of("Los Angeles"))
      .displayedNameForPlaceOfReceipt(List.of("Hong Kong"))
      .displayedNameForPortOfLoad(List.of("Port of Hong Kong"))
      .displayedNameForPortOfDischarge(List.of("Port of Long Beach"))
      .build();

    return List.of(shippingInstructionSummaryTO, singleShippingInstructionSummaryTO());
  }
}
