package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.*;
import org.dcsa.skernel.infrastructure.validation.ValidEnum;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record BookingSummaryTO(
  String carrierBookingRequestReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  BkgDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  OffsetDateTime bookingRequestCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  OffsetDateTime bookingRequestUpdatedDateTime,

  @NotNull(message = "The attribute receiptTypeAtOrigin is required.")
  ReceiptDeliveryType receiptTypeAtOrigin,

  @NotNull(message = "The attribute deliveryTypeAtDestination is required.")
  ReceiptDeliveryType deliveryTypeAtDestination,

  @NotNull(message = "The attribute cargoMovementTypeAtOrigin is required.")
  CargoMovementType cargoMovementTypeAtOrigin,

  @NotNull(message = "The attribute cargoMovementTypeAtDestination is required.")
  CargoMovementType cargoMovementTypeAtDestination,

  @NotBlank(message = "The attribute serviceContractReference is required.")
  @Size(max = 30, message = "The attribute serviceContractReference has a max size of 30.")
  String serviceContractReference,

  PaymentTerm paymentTermCode,

  @NotNull(message = "The attribute isPartialLoadAllowed is required.")
  Boolean isPartialLoadAllowed,

  @NotNull(message = "The attribute isExportDeclarationRequired is required.")
  Boolean isExportDeclarationRequired,

  String exportDeclarationReference,

  @NotNull(message = "The attribute isImportLicenseRequired is required.")
  Boolean isImportLicenseRequired,

  @Size(max = 35, message = "The attribute importLicenseReference has a max size of 35.")
  String importLicenseReference,

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @NotNull(message = "The attribute submissionDateTime is required.")
  OffsetDateTime submissionDateTime,

  Boolean isAMSACIFilingRequired,

  Boolean isDestinationFilingRequired,

  @Size(max = 35, message = "The attribute contractQuotationReference has a max size of 35.")
  String contractQuotationReference,

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  LocalDate expectedDepartureDate,

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  LocalDate expectedArrivalAtPlaceOfDeliveryStartDate,

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  LocalDate expectedArrivalAtPlaceOfDeliveryEndDate,

  TransportDocumentTypeCode transportDocumentTypeCode,

  @Size(max = 20, message = "The attribute transportDocumentReference has a max size of 20.")
  String transportDocumentReference,

  @Size(max = 20, message = "The attribute bookingChannelReference has a max size of 20.")
  String bookingChannelReference,

  IncoTerms incoTerms,

  @NotNull(message = "The attribute communicationChannelCode is required.")
  CommunicationChannelCode communicationChannelCode,

  @NotNull(message = "The attribute isEquipmentSubstitutionAllowed is required.")
  Boolean isEquipmentSubstitutionAllowed,

  @Size(max = 35, message = "The attribute vesselName has a max size of 35.")
  String vesselName,

  @ValidVesselIMONumber(allowNull = true, message = "The attribute vesselIMONumber is invalid.")
  String vesselIMONumber,

  @Size(max = 50, message = "The attribute exportVoyageNumber has a max size of 50.")
  String exportVoyageNumber,

  DCSATransportType preCarriageModeOfTransportCode
) {
  @Builder
  public BookingSummaryTO { }
}
