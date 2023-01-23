package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.CargoMovementType;
import org.dcsa.edocumentation.transferobjects.enums.CommunicationChannelCode;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.IncoTerms;
import org.dcsa.edocumentation.transferobjects.enums.PaymentTerm;
import org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.LocationType;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;
import org.dcsa.skernel.infrastructure.validation.AtLeast;
import org.dcsa.skernel.infrastructure.validation.DateRange;
import org.dcsa.skernel.infrastructure.validation.RequiredIfTrue;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@AtLeast(
  nonNullsRequired = 1,
  fields = {"serviceContractReference", "contractQuotationReference"}
)
@AtLeast(
  nonNullsRequired = 1,
  fields = {
    "expectedArrivalAtPlaceOfDeliveryStartDate",
    "expectedArrivalAtPlaceOfDeliveryEndDate",
    "expectedDepartureDate",
    "vesselIMONumber",
    "carrierExportVoyageNumber"
  }
)
@AllOrNone({"declaredValue", "declaredValueCurrency"})
@DateRange(startField = "expectedDepartureDate", endField = "expectedArrivalAtPlaceOfDeliveryStartDate")
@DateRange(startField = "expectedArrivalAtPlaceOfDeliveryStartDate", endField = "expectedArrivalAtPlaceOfDeliveryEndDate")
@RequiredIfTrue(isFieldReferenceRequired = "isExportDeclarationRequired", fieldReference = "exportDeclarationReference")
@RequiredIfTrue(isFieldReferenceRequired = "isImportLicenseRequired", fieldReference = "importLicenseReference")
public record BookingTO(
  /* Must be null in POST, must be non-null in PUT and GET */
  @Size(max = 100)
  String carrierBookingRequestReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  BkgDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime bookingRequestCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime bookingRequestUpdatedDateTime,

  @NotNull
  ReceiptDeliveryType receiptTypeAtOrigin,

  @NotNull
  ReceiptDeliveryType deliveryTypeAtDestination,

  @NotNull
  CargoMovementType cargoMovementTypeAtOrigin,

  @NotNull
  CargoMovementType cargoMovementTypeAtDestination,

  @Size(max = 30)
  String serviceContractReference,

  @Size(max = 50)
  String carrierExportVoyageNumber,

  @Pattern(regexp = "\\d{2}[0-9A-Z]{2}[NEWS]", message = "Not a valid voyage reference")
  String universalExportVoyageReference,

  @Size(max = 11)
  String carrierServiceCode,

  @UniversalServiceReference
  String universalServiceReference,

  Float declaredValue,

  @Size(max = 3)
  String declaredValueCurrency,

  PaymentTerm paymentTermCode,

  @NotNull
  Boolean isPartialLoadAllowed,

  @NotNull
  Boolean isExportDeclarationRequired,

  @Size(max = 35)
  String exportDeclarationReference,

  @NotNull
  Boolean isImportLicenseRequired,

  @Size(max = 35)
  String importLicenseReference,

  boolean isAMSACIFilingRequired,

  boolean isDestinationFilingRequired,

  @Size(max = 35)
  String contractQuotationReference,

  LocalDate expectedDepartureDate,

  LocalDate expectedArrivalAtPlaceOfDeliveryStartDate,

  LocalDate expectedArrivalAtPlaceOfDeliveryEndDate,

  TransportDocumentTypeCode transportDocumentTypeCode,

  @Size(max = 20)
  String transportDocumentReference,

  @Size(max = 20)
  String bookingChannelReference,

  IncoTerms incoTerms,

  @NotNull
  CommunicationChannelCode communicationChannelCode,

  @NotNull
  Boolean isEquipmentSubstitutionAllowed,

  @ValidVesselIMONumber(allowNull = true)
  String vesselIMONumber,

  DCSATransportType preCarriageModeOfTransportCode,

  @Valid
  @RestrictLocationTO({LocationType.ADDRESS, LocationType.UNLOCATION})
  LocationTO invoicePayableAt,

  @Valid
  @RestrictLocationTO({LocationType.ADDRESS, LocationType.UNLOCATION})
  LocationTO placeOfBLIssue,

  @NotEmpty @Valid
  List<CommodityTO> commodities,

  @Valid
  List<ValueAddedServiceRequestTO> valueAddedServiceRequests,

  @Valid
  List<ReferenceTO> references,

  @Valid
  List<DocumentPartyTO> documentParties,

  @Valid
  List<ShipmentLocationTO> shipmentLocations
) {
  @Builder(toBuilder = true)
  public BookingTO { }
}
