package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.*;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;
import org.dcsa.skernel.infrastructure.validation.AtLeast;
import org.dcsa.skernel.infrastructure.validation.DateRange;
import org.dcsa.skernel.infrastructure.validation.RequiredIfTrue;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

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
  String bookingStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<RequestedChangeTO> requestedChanges,

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

  @Size(max = 3)
  String incoTerms,

  @NotNull
  @Size(max = 2)
  String communicationChannelCode,

  @NotNull
  Boolean isEquipmentSubstitutionAllowed,

  @ValidVesselIMONumber(allowNull = true)
  String vesselIMONumber,

  @Valid
  LocationTO invoicePayableAt,

  @Valid
  LocationTO placeOfBLIssue,

  @NotEmpty
  List<@Valid RequestedEquipmentTO> requestedEquipments,

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
