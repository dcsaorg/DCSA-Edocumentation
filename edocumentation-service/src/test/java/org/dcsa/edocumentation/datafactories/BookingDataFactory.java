package org.dcsa.edocumentation.datafactories;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CargoMovementType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CommunicationChannelCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.IncoTerms;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PaymentTerm;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReceiptDeliveryType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;

@UtilityClass
public class BookingDataFactory {

  public BookingRequest singleShallowBooking() {
    return BookingRequest.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .bookingStatus(BookingStatus.RECEIVED)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestCreatedDateTime(OffsetDateTime.now())
        .serviceContractReference("serviceRef")
        .paymentTermCode(PaymentTerm.COL)
        .isPartialLoadAllowed(false)
        .isExportDeclarationRequired(true)
        .exportDeclarationReference("exportRef")
        .isImportLicenseRequired(true)
        .importLicenseReference("importRef")
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
        .declaredValueCurrency("EUR")
        .declaredValue(10000F)
        .build();
  }

  public BookingRequest singleShallowBookingWithVesselAndModeOfTransport() {
    Vessel mockVessel = VesselDataFactory.vessel();

    return BookingRequest.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .bookingStatus(BookingStatus.RECEIVED)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestCreatedDateTime(OffsetDateTime.now())
        .serviceContractReference("serviceRef")
        .paymentTermCode(PaymentTerm.COL)
        .isPartialLoadAllowed(false)
        .isExportDeclarationRequired(true)
        .exportDeclarationReference("exportRef")
        .isImportLicenseRequired(true)
        .importLicenseReference("importRef")
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
        .declaredValueCurrency("EUR")
        .declaredValue(10000F)
        .vessel(mockVessel)
        .build();
  }

  public List<BookingRequest> multipleShallowBookingsWithVesselAndModeOfTransport() {
    Vessel mockVessel = VesselDataFactory.vessel();

    BookingRequest bookingRequest1 =
        BookingRequest.builder()
            .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
            .carrierBookingRequestReference("BOOKING_REQ_REF_01")
            .bookingStatus(BookingStatus.RECEIVED)
            .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
            .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
            .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
            .cargoMovementTypeAtDestination(CargoMovementType.FCL)
            .bookingRequestCreatedDateTime(OffsetDateTime.now())
            .serviceContractReference("serviceRef")
            .paymentTermCode(PaymentTerm.COL)
            .isPartialLoadAllowed(false)
            .isExportDeclarationRequired(true)
            .exportDeclarationReference("exportRef")
            .isImportLicenseRequired(true)
            .importLicenseReference("importRef")
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
            .declaredValueCurrency("EUR")
            .declaredValue(10000F)
            .vessel(mockVessel)
            .build();

    BookingRequest bookingRequest2 =
        BookingRequest.builder()
            .id(UUID.fromString("3262f836-65d1-43e1-b759-dbeb53ac24ee"))
            .carrierBookingRequestReference("BOOKING_REQ_REF_02")
            .bookingStatus(BookingStatus.RECEIVED)
            .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
            .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
            .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
            .cargoMovementTypeAtDestination(CargoMovementType.FCL)
            .bookingRequestCreatedDateTime(OffsetDateTime.now())
            .serviceContractReference("serviceRef")
            .paymentTermCode(PaymentTerm.COL)
            .isPartialLoadAllowed(false)
            .isExportDeclarationRequired(true)
            .exportDeclarationReference("exportRef2")
            .isImportLicenseRequired(true)
            .importLicenseReference("importRef")
            .isAMSACIFilingRequired(true)
            .isDestinationFilingRequired(true)
            .contractQuotationReference("contractRef2")
            .incoTerms(IncoTerms.FCA.name())
            .expectedDepartureDate(LocalDate.of(2023, 10, 12))
            .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.of(2023, 11, 9))
            .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.of(2023, 11, 15))
            .transportDocumentTypeCode(TransportDocumentTypeCode.BOL)
            .communicationChannelCode(CommunicationChannelCode.AO.name())
            .isEquipmentSubstitutionAllowed(false)
            .declaredValueCurrency("EUR")
            .declaredValue(2000F)
            .vessel(mockVessel)
            .build();

    return List.of(bookingRequest1, bookingRequest2);
  }

  public BookingRequest singleDeepBooking() {
    Vessel mockVessel = VesselDataFactory.vessel();

    return BookingRequest.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .bookingStatus(BookingStatus.RECEIVED)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestCreatedDateTime(OffsetDateTime.now())
        .serviceContractReference("serviceRef")
        .paymentTermCode(PaymentTerm.COL)
        .isPartialLoadAllowed(false)
        .isExportDeclarationRequired(true)
        .exportDeclarationReference("exportRef")
        .isImportLicenseRequired(true)
        .importLicenseReference("importRef")
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
        .declaredValueCurrency("EUR")
        .declaredValue(10000F)
        .vessel(mockVessel)
        .requestedEquipments(List.of(RequestedEquipmentDataFactory.singleRequestedEquipment()))
        .references(Set.of(ReferenceDataFactory.singleReference()))
        .documentParties(Set.of(DocumentPartyDataFactory.singleDocumentParty()))
        .shipmentLocations(Set.of(ShipmentLocationDataFactory.singleShipmentLocation()))
        .build();
  }

  public BookingRequest singleMinimalBooking() {
    return BookingRequest.builder()
        .id(UUID.fromString("1bc6f4d1-c728-4504-89fe-98ab10aaf5fd"))
        .carrierBookingRequestReference("BOOKING_REQ_REF_01")
        .bookingStatus(BookingStatus.RECEIVED)
        .receiptTypeAtOrigin(ReceiptDeliveryType.CY)
        .deliveryTypeAtDestination(ReceiptDeliveryType.CY)
        .cargoMovementTypeAtOrigin(CargoMovementType.FCL)
        .cargoMovementTypeAtDestination(CargoMovementType.FCL)
        .bookingRequestCreatedDateTime(OffsetDateTime.now())
        .serviceContractReference("serviceRef")
        .isPartialLoadAllowed(false)
        .isExportDeclarationRequired(false)
        .isImportLicenseRequired(false)
        .communicationChannelCode(CommunicationChannelCode.AO.name())
        .isEquipmentSubstitutionAllowed(false)
        .requestedEquipments(List.of(RequestedEquipmentDataFactory.singleRequestedEquipment()))
        .build();
  }

  public BookingRequestTO singleFullBookingRequestTO() {
    return BookingRequestTO.builder()
      .receiptTypeAtOrigin(org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType.CY)
      .deliveryTypeAtDestination(org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType.CY)
      .cargoMovementTypeAtDestination(org.dcsa.edocumentation.transferobjects.enums.CargoMovementType.FCL)
      .serviceContractReference("serviceRef")
      .carrierExportVoyageNumber("voyageRef")
      .universalExportVoyageReference("35BQE")
      .declaredValue(3.14f)
      .declaredValueCurrency("DKK")
      .paymentTermCode(org.dcsa.edocumentation.transferobjects.enums.PaymentTerm.PRE)
      .isPartialLoadAllowed(true)
      .isExportDeclarationRequired(true)
      .exportDeclarationReference("exportDeclarationRef")
      .isImportLicenseRequired(true)
      .importLicenseReference("importLicenseRef")
      .isAMSACIFilingRequired(true)
      .isDestinationFilingRequired(true)
      .contractQuotationReference("contractQuotationRef")
      .expectedDepartureDate(LocalDate.now())
      .expectedArrivalAtPlaceOfDeliveryStartDate(LocalDate.now())
      .expectedArrivalAtPlaceOfDeliveryEndDate(LocalDate.now())
      .transportDocumentTypeCode(org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode.BOL)
      .transportDocumentReference("transportDocumentRef")
      .bookingChannelReference("bookingChannelRef")
      .incoTerms(org.dcsa.edocumentation.transferobjects.enums.IncoTerms.FOB.name())
      .communicationChannelCode(org.dcsa.edocumentation.transferobjects.enums.CommunicationChannelCode.AO.name())
      .isEquipmentSubstitutionAllowed(true)
      .vesselIMONumber("1234567")
      .invoicePayableAt(LocationDataFactory.addressLocationTO())
      .placeOfBLIssue(LocationDataFactory.addressLocationTO())
      .requestedEquipments(List.of(RequestedEquipmentDataFactory.requestedEquipmentTORef1()))
      .references(List.of(ReferenceDataFactory.singleReferenceTO()))
      .documentParties(List.of(DocumentPartyDataFactory.fullDocumentPartyTO()))
      .shipmentLocations(List.of(ShipmentLocationDataFactory.shipmentLocationTO()))
      .build();
  }
}
