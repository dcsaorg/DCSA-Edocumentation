package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.*;
import org.dcsa.edocumentation.transferobjects.validation.EBLValidation;
import org.dcsa.edocumentation.transferobjects.validation.PaperBLValidation;
import org.dcsa.edocumentation.transferobjects.validation.TransportDocumentTOValidation;

@TransportDocumentTOValidation
public record TransportDocumentTO(
  @NotNull
  @Size(max = 20)
  @Pattern(regexp = "^\\S+(\\s+\\s+)*$")
  String transportDocumentReference,
  @NotNull
  TransportDocumentTypeCode transportDocumentTypeCode,
  @NotNull
  EblDocumentStatus documentStatus,

  LocalDate issueDate,

  LocalDate shippedOnBoardDate,
  LocalDate receivedForShipmentDate,
  @NotNull
  @Size(max = 4)
  @Pattern(regexp = "^\\S+$")
  String carrierCode,
  @NotNull
  CarrierCodeListProvider carrierCodeListProvider,
  @NotNull
  PartyTO issuingParty,
  @Min(0)
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  Integer numberOfRiderPages,
  @NotNull
  @Size(max = 20000)
  String termsAndConditions,
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
  @Size(max = 30)
  @Pattern(regexp = "^\\S+(\\s+\\S+)$")
  String contractQuotationReference,

  @Min(0)
  Float declaredValue,

  @Size(max = 3)
  @Pattern(regexp = "^[A-Z]{3}$")
  String declaredValueCurrency,

  @NotNull
  LocationTO invoicePayableAt,

  @Min(0)
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  Integer numberOfCopiesWithCharges,
  @Min(0)
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  Integer numberOfCopiesWithoutCharges,

  @Min(0)
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  Integer numberOfOriginalsWithCharges,
  @Min(0)
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  Integer numberOfOriginalsWithoutCharges,

  @NotNull(message = "isElectronic is required.")
  Boolean isElectronic,

  @NotNull(message = "isToOrder is required.")
  Boolean isToOrder,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfReceipt,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfLoad,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfDischarge,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfDelivery,

  LocationTO placeOfIssue,

  @Valid
  @NotEmpty(message = "consignmentItems are required.")
  List<@Valid @NotNull ConsignmentItemTO> consignmentItems,

  @Valid
  @NotEmpty(message = "utilizedTransportEquipments are required.")
  List<@Valid @NotNull UtilizedTransportEquipmentTO> utilizedTransportEquipments,

  @Valid
  @NotEmpty
  List<@Valid @NotNull DocumentPartyTO> documentParties,

  @NotNull
  TDTransportTO transports,

  @Valid
  List<@Valid @NotNull ReferenceTO> references,

  List<@Valid @NotNull CustomsReferenceTO> customsReferences,

  @Valid
  List<@Valid ChargeTO> charges,

  List<@Pattern(regexp = "^\\S+(\\s+\\S+)$") @Size(max = 20000) CarrierClauseTO> carrierClauses
) {
  @Builder(toBuilder = true)
  public TransportDocumentTO{}

}
