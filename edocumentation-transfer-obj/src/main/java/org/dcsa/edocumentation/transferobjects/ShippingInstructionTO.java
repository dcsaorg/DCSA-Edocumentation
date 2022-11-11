package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.infrastructure.validation.RequiredIfTrue;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredIfTrue(isFieldReferenceRequired = "isElectronic", fieldReference = "numberOfCopiesWithCharges")
@RequiredIfTrue(isFieldReferenceRequired = "isElectronic", fieldReference = "numberOfCopiesWithoutCharges")
@RequiredIfTrue(isFieldReferenceRequired = "isElectronic", fieldReference = "numberOfOriginalsWithCharges")
@RequiredIfTrue(isFieldReferenceRequired = "isElectronic", fieldReference = "numberOfOriginalsWithoutCharges")
public record ShippingInstructionTO(
  @Size(max = 35)
  String carrierBookingReference,

  @Size(max = 100)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String shippingInstructionReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  EblDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime shippingInstructionCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime shippingInstructionUpdatedDateTime,

  @NotNull(message = "isShippedOnBoardType is required.")
  Boolean isShippedOnBoardType,

  Integer numberOfCopiesWithCharges,
  Integer numberOfCopiesWithoutCharges,

  Integer numberOfOriginalsWithCharges,
  Integer numberOfOriginalsWithoutCharges,

  @NotNull(message = "isElectronic is required.")
  Boolean isElectronic,

  @NotNull(message = "isToOrder is required.")
  Boolean isToOrder,

  Boolean areChargesDisplayedOnOriginals,

  Boolean areChargesDisplayedOnCopies,

  @Valid
  LocationTO placeOfIssue,

  TransportDocumentTypeCode transportDocumentTypeCode,

  @Size(max = 250)
  String displayedNameForPlaceOfReceipt,

  @Size(max = 250)
  String displayedNameForPortOfLoad,

  @Size(max = 250)
  String displayedNameForPortOfDischarge,

  @Size(max = 250)
  String displayedNameForPlaceOfDelivery,

  UUID amendmentToTransportDocument,

  @Valid
  @NotNull(message = "consigmentItems are required.")
  List<ConsignmentItemTO>consignmentItems,

  @Valid
  @NotNull(message = "utilizedTransportEquipments are required.")
  List<UtilizedTransportEquipmentTO> utilizedTransportEquipments,

  @Valid
  List<DocumentPartyTO> documentParties,

  List<ReferenceTO> references
) {
  @Builder
  public ShippingInstructionTO{}
}
