package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.RequiredIfFalse;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredIfFalse(
  ifFalse = "isElectronic",
  thenNotNull = {
    "numberOfCopiesWithCharges",
    "numberOfCopiesWithoutCharges",
    "numberOfOriginalsWithCharges",
    "numberOfOriginalsWithoutCharges"
  }
)
public record ShippingInstructionTO(
  @Size(max = 35)
  String carrierBookingReference,

  @Size(max = 100)
  String shippingInstructionReference,

  EblDocumentStatus documentStatus,

  OffsetDateTime shippingInstructionCreatedDateTime,

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

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfReceipt,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfLoad,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPortOfDischarge,

  @Size(max = 5)
  List<@Size(max = 35) String> displayedNameForPlaceOfDelivery,

  UUID amendmentToTransportDocument,

  @Valid
  @NotEmpty(message = "consignmentItems are required.")
  List<ConsignmentItemTO>consignmentItems,

  @Valid
  @NotEmpty(message = "utilizedTransportEquipments are required.")
  List<UtilizedTransportEquipmentTO> utilizedTransportEquipments,

  @Valid
  List<DocumentPartyTO> documentParties,

  List<ReferenceTO> references
) {
  @Builder(toBuilder = true)
  public ShippingInstructionTO{}
}
