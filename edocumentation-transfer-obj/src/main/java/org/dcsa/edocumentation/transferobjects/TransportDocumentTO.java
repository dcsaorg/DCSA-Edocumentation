package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.CargoMovementType;
import org.dcsa.edocumentation.transferobjects.enums.CarrierCodeListProvider;
import org.dcsa.edocumentation.transferobjects.enums.ReceiptDeliveryType;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record TransportDocumentTO(
  String transportDocumentReference,
  OffsetDateTime transportDocumentCreatedDateTime,
  OffsetDateTime transportDocumentUpdatedDateTime,
  LocalDate issueDate,
  LocalDate shippedOnBoardDate,
  LocalDate receivedForShipmentDate,
  Integer numberOfOriginalsWithCharges,
  Integer numberOfOriginalsWithoutCharges,
  String carrierCode,
  CarrierCodeListProvider carrierCodeListProvider,
  PartyTO issuingParty,
  Integer numberOfRiderPages,
  String termsAndConditions,
  ReceiptDeliveryType receiptTypeAtOrigin,
  ReceiptDeliveryType deliveryTypeAtDestination,
  CargoMovementType cargoMovementTypeAtOrigin,
  CargoMovementType cargoMovementTypeAtDestination,
  String serviceContractReference,
  String vesselName,
  String carrierExportVoyageNumber,
  String universalExportVoyageReference,
  Float declaredValue,
  String declaredValueCurrency,
  List<TransportTO> transports,
  List<ShipmentLocationTO> shipmentLocations,
  LocationTO invoicePayableAt,
  LocationTO placeOfIssue,
  ShippingInstructionTO shippingInstruction,
  List<ChargeTO> charges,
  List<CarrierClauseTO> carrierClauses
) {
  @Builder(toBuilder = true)
  public TransportDocumentTO{}
}
