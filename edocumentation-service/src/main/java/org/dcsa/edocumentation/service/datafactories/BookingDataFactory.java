package org.dcsa.edocumentation.service.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.skernel.domain.persistence.entity.enums.DimensionUnit;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class BookingDataFactory {

  public Booking singleShallowBooking() {
    return Booking.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .documentStatus(BkgDocumentStatus.RECE)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestDateTime(OffsetDateTime.now())
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
        .incoTerms(IncoTerms.FCA)
        .expectedDepartureDate(LocalDate.of(2023, 10, 12))
        .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
        .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
        .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
        .communicationChannelCode(CommunicationChannelCode.AO)
        .isEquipmentSubstitutionAllowed(false)
        .declaredValueCurrency("EUR")
        .declaredValue(10000F)
        .preCarriageModeOfTransportCode("1")
        .build();
  }

  public Booking singleShallowBookingWithVessel() {
    Vessel mockVessel =
        Vessel.builder()
            .id(UUID.fromString("f90de80b-af26-4703-93b0-722bb3fa69c7"))
            .vesselIMONumber("1234567")
            .name("Mock Vessel")
            .flag("NL")
            .callSignNumber("123456789012345678")
            .isDummy(false)
            .length(120F)
            .width(40F)
            .dimensionUnit(DimensionUnit.MTR)
            .type(VesselType.CONT)
            .build();

    return Booking.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .documentStatus(BkgDocumentStatus.RECE)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestDateTime(OffsetDateTime.now())
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
        .incoTerms(IncoTerms.FCA)
        .expectedDepartureDate(LocalDate.of(2023, 10, 12))
        .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
        .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
        .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
        .communicationChannelCode(CommunicationChannelCode.AO)
        .isEquipmentSubstitutionAllowed(false)
        .declaredValueCurrency("EUR")
        .declaredValue(10000F)
        .preCarriageModeOfTransportCode("1")
        .vessel(mockVessel)
        .build();
  }

  public List<Booking> multipleShallowBookingsWithVessel() {
    Vessel mockVessel =
        Vessel.builder()
            .id(UUID.fromString("f90de80b-af26-4703-93b0-722bb3fa69c7"))
            .vesselIMONumber("1234567")
            .name("Mock Vessel")
            .flag("NL")
            .callSignNumber("123456789012345678")
            .isDummy(false)
            .length(120F)
            .width(40F)
            .dimensionUnit(DimensionUnit.MTR)
            .type(VesselType.CONT)
            .build();

    Booking booking1 =
        Booking.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
            .carrierBookingRequestReference("BOOKING_REQ_REF_01")
            .documentStatus(BkgDocumentStatus.RECE)
            .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
            .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
            .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
            .cargoMovementTypeAtDestination(CargoMovementType.FCL)
            .bookingRequestDateTime(OffsetDateTime.now())
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
            .incoTerms(IncoTerms.FCA)
            .expectedDepartureDate(LocalDate.of(2023, 10, 12))
            .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
            .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
            .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
            .communicationChannelCode(CommunicationChannelCode.AO)
            .isEquipmentSubstitutionAllowed(false)
            .declaredValueCurrency("EUR")
            .declaredValue(10000F)
            .preCarriageModeOfTransportCode("1")
            .vessel(mockVessel)
            .build();

    Booking booking2 =
        Booking.builder()
            .id(UUID.fromString("3262f836-65d1-43e1-b759-dbeb53ac24ee"))
            .carrierBookingRequestReference("BOOKING_REQ_REF_02")
            .documentStatus(BkgDocumentStatus.RECE)
            .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
            .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
            .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
            .cargoMovementTypeAtDestination(CargoMovementType.FCL)
            .bookingRequestDateTime(OffsetDateTime.now())
            .serviceContractReference("serviceRef")
            .paymentTermCode(PaymentTerm.COL)
            .isPartialLoadAllowed(false)
            .isExportDeclarationRequired(true)
            .exportDeclarationReference("exportRef2")
            .isImportLicenseRequired(true)
            .importLicenseReference("importRef")
            .submissionDateTime(OffsetDateTime.now())
            .isAMSACIFilingRequired(true)
            .isDestinationFilingRequired(true)
            .contractQuotationReference("contractRef2")
            .incoTerms(IncoTerms.FCA)
            .expectedDepartureDate(LocalDate.of(2023, 10, 12))
            .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
            .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
            .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
            .communicationChannelCode(CommunicationChannelCode.AO)
            .isEquipmentSubstitutionAllowed(false)
            .declaredValueCurrency("EUR")
            .declaredValue(2000F)
            .preCarriageModeOfTransportCode("1")
            .vessel(mockVessel)
            .build();

    return List.of(booking1, booking2);
  }
}
