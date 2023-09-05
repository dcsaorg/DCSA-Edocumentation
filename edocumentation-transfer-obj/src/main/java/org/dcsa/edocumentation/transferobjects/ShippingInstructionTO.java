package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.RequiredIfFalse;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

public record ShippingInstructionTO(
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

  @Min(0)
  Integer numberOfCopiesWithCharges,
  @Min(0)
  Integer numberOfCopiesWithoutCharges,

  @Min(0)
  Integer numberOfOriginalsWithCharges,
  @Min(0)
  Integer numberOfOriginalsWithoutCharges,

  @Valid
  @RestrictLocationTO({LocationTO.LocationType.ADDRESS, LocationTO.LocationType.UNLOCATION})
  LocationTO invoicePayableAt,

  @NotNull(message = "isElectronic is required.")
  Boolean isElectronic,

  @NotNull(message = "isToOrder is required.")
  Boolean isToOrder,

  Boolean areChargesDisplayedOnOriginals,

  Boolean areChargesDisplayedOnCopies,

  @Valid
  LocationTO placeOfIssue,

  TransportDocumentTypeCode transportDocumentTypeCode,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfReceipt,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfLoad,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfDischarge,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfDelivery,

  @Size(max = 20)
  @Pattern(regexp = "^\\S+(\\s+\\S+)$")
  String amendmentToTransportDocument,

  @Valid
  @NotEmpty(message = "consignmentItems are required.")
  List<@Valid @NotNull ConsignmentItemTO> consignmentItems,

  @Valid
  @NotEmpty(message = "utilizedTransportEquipments are required.")
  List<@Valid @NotNull UtilizedTransportEquipmentTO> utilizedTransportEquipments,

  @Valid
  List<@Valid @NotNull DocumentPartyTO> documentParties,

  @Valid
  List<@Valid @NotNull ReferenceTO> references,

  @Valid
  List<@Valid @NotNull CustomsReferenceTO> customsReferences
) {
  @Builder
  public ShippingInstructionTO{}
}
