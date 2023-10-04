package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@UtilityClass
public class BookingSummaryDataFactory {

  public BookingSummaryTO singleBookingSummaryTO() {
    return BookingSummaryTO.builder()
      .carrierBookingRequestReference("BOOKING_REQ_REF_01")
      .bookingStatus(BookingStatus.RECEIVED)
      .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
      .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
      .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
      .cargoMovementTypeAtDestination(CargoMovementType.FCL)
      .serviceContractReference("serviceRef")
      .paymentTermCode(PaymentTerm.COL)
      .isPartialLoadAllowed(false)
      .isExportDeclarationRequired(true)
      .exportDeclarationReference("exportRef")
      .isImportLicenseRequired(true)
      .importLicenseReference("importRef")
      .submissionDateTime(OffsetDateTime.now())
      .isAMSACIFilingRequired(true)
      .isDestinationFilingRequired(true)
      .contractQuotationReference("contractRef")
      .incoTerms(IncoTerms.FCA.name())
      .expectedDepartureDate(LocalDate.of(2023, 10, 12))
      .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
      .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .communicationChannelCode(CommunicationChannelCode.AO.name())
      .isEquipmentSubstitutionAllowed(false)
      .preCarriageUnderShippersResponsibility(DCSATransportType.VESSEL.name())
      .vesselIMONumber("1234567")
      .build();
  }

  public List<BookingSummaryTO> multipleBookingSummaryTos() {
    BookingSummaryTO bookingSummaryTO1 = BookingSummaryTO.builder()
      .carrierBookingRequestReference("BOOKING_REQ_REF_01")
      .bookingStatus(BookingStatus.RECEIVED)
      .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
      .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
      .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
      .cargoMovementTypeAtDestination(CargoMovementType.FCL)
      .serviceContractReference("serviceRef")
      .paymentTermCode(PaymentTerm.COL)
      .isPartialLoadAllowed(false)
      .isExportDeclarationRequired(true)
      .exportDeclarationReference("exportRef")
      .isImportLicenseRequired(true)
      .importLicenseReference("importRef")
      .submissionDateTime(OffsetDateTime.now())
      .isAMSACIFilingRequired(true)
      .isDestinationFilingRequired(true)
      .contractQuotationReference("contractRef")
      .incoTerms(IncoTerms.FCA.name())
      .expectedDepartureDate(LocalDate.of(2023, 10, 12))
      .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
      .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .communicationChannelCode(CommunicationChannelCode.AO.name())
      .isEquipmentSubstitutionAllowed(false)
      .preCarriageUnderShippersResponsibility(DCSATransportType.VESSEL.name())
      .vesselIMONumber("1234567")
      .build();

    BookingSummaryTO bookingSummaryTO2 = BookingSummaryTO.builder()
      .carrierBookingRequestReference("BOOKING_REQ_REF_02")
      .bookingStatus(BookingStatus.RECEIVED)
      .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
      .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
      .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
      .cargoMovementTypeAtDestination(CargoMovementType.FCL)
      .serviceContractReference("serviceRef")
      .paymentTermCode(PaymentTerm.COL)
      .isPartialLoadAllowed(false)
      .isExportDeclarationRequired(true)
      .exportDeclarationReference("exportRef")
      .isImportLicenseRequired(true)
      .importLicenseReference("importRef")
      .submissionDateTime(OffsetDateTime.now())
      .isAMSACIFilingRequired(true)
      .isDestinationFilingRequired(true)
      .contractQuotationReference("contractRef")
      .incoTerms(IncoTerms.FCA.name())
      .expectedDepartureDate(LocalDate.of(2023, 10, 12))
      .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
      .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
      .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
      .communicationChannelCode(CommunicationChannelCode.AO.name())
      .isEquipmentSubstitutionAllowed(false)
      .preCarriageUnderShippersResponsibility(DCSATransportType.VESSEL.name())
      .vesselIMONumber("1234567")
      .build();

    return List.of(bookingSummaryTO1, bookingSummaryTO2);
  }
}
