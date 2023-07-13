package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.*;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

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
  @RestrictLocationTO({
    LocationTO.LocationType.UNLOCATION,
    LocationTO.LocationType.ADDRESS,
  })
  LocationTO invoicePayableAt,

  @Min(0)
  Integer numberOfCopiesWithCharges,
  @Min(0)
  Integer numberOfCopiesWithoutCharges,

  @Min(0)
  Integer numberOfOriginalsWithCharges,
  @Min(0)
  Integer numberOfOriginalsWithoutCharges,

  // TODO: preCarriageUnderShippersResponsibility here

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

  @RestrictLocationTO({
    LocationTO.LocationType.UNLOCATION,
    LocationTO.LocationType.ADDRESS,
  })
  LocationTO placeOfIssue,

  @Valid
  @NotEmpty(message = "consignmentItems are required.")
  List<@Valid @NotNull ConsignmentItemTO> consignmentItems,

  @Valid
  @NotEmpty(message = "utilizedTransportEquipments are required.")
  List<@Valid @NotNull UtilizedTransportEquipmentTO> utilizedTransportEquipments,

  @Valid
  List<@Valid @NotNull DocumentPartyTO> documentParties,

  // TODO: TransportLeg here
  // TODO: transports have a different definition in the swagger
  @NotNull
  TDTransportTO transports,

  @Valid
  List<@Valid @NotNull ReferenceTO> references,

  //TODO: Add customs references here

  @Valid
  List<@Valid ChargeTO> charges,
  List<@Pattern(regexp = "^\\S+(\\s+\\S+)$") @Size(max = 20000) CarrierClauseTO> carrierClauses
) {
  @Builder(toBuilder = true)
  public TransportDocumentTO{}

  //FIXME: This really belongs in the domain object, but we do not share the domain object between the issuance and
  // the ebl data standard, so that is "meh" to say the least.  Additionally, all validations we do in the issuance
  // API must be doable from the TO alone (the platform receiving the TD does not have the same context as the
  // carrier does).
  public void validateConsistency() {
    verifyConsistency(!hasBeenIssued() || issueDate != null,
      "Issued transport document is missing an issueDate"
    );
    validatePaperOnlyAttributes();
    if (hasBeenIssued()) {
      validateShippedOnBoardVsReceivedForShipment();
    }
    validateContractQuotation();
    validateShipper();
    if (isToOrder == Boolean.TRUE) {
      validateNegotiableBL();
    } else {
      validateStraightBL();
    }
  }

  public boolean hasBeenIssued() {
    // ISSU and any status after it.
    return documentStatus == EblDocumentStatus.ISSU
      || documentStatus == EblDocumentStatus.SURR
      || documentStatus == EblDocumentStatus.VOID;
  }

  private void validatePaperOnlyAttributes() {
    if (this.isElectronic == Boolean.TRUE) {
      verifyConsistency(this.numberOfRiderPages == null, "Electronic B/Ls cannot have rider pages");
      verifyConsistency(this.numberOfCopiesWithCharges == null, "Electronic B/Ls cannot have copies (w. charges)");
      verifyConsistency(this.numberOfCopiesWithoutCharges == null, "Electronic B/Ls cannot have copies (w.o. charges)");
      verifyConsistency(this.numberOfOriginalsWithCharges == null, "Electronic B/Ls cannot have multiple originals (w. charges)");
      verifyConsistency(this.numberOfOriginalsWithoutCharges == null, "Electronic B/Ls cannot have multiple originals (w.o. charges)");
    } else {
      verifyConsistency(this.numberOfRiderPages != null, "Paper B/Ls must list how many rider pages it has.");
      verifyConsistency(this.numberOfCopiesWithCharges != null, "Paper B/Ls must list how many copies with charges it has.");
      verifyConsistency(this.numberOfCopiesWithoutCharges != null, "Paper B/Ls must list how many copies without charges it has.");
      verifyConsistency(this.numberOfOriginalsWithCharges != null, "Paper B/Ls must list how many originals with charges it has.");
      verifyConsistency(this.numberOfOriginalsWithoutCharges != null, "Paper B/Ls must list how many originals without charges it has.");
    }
  }

  private void validateContractQuotation() {
    if (contractQuotationReference == null) {
      var sco = nullSafeStream(documentParties).filter(p -> p.partyFunction() == PartyFunction.SCO).toList();
      verifyConsistency(!sco.isEmpty(),
        "Without a contractQuotationReference, a service contract owner must be present"
      );
    }
  }

  private void validateStraightBL() {
    // TODO: Does DDS (consignee freight forwarder) also count?
    // NB: Currently if both are present, we do not know which of them is the "real" consignee
    Predicate<DocumentPartyTO> isConsigneeOrCFF = p -> p.partyFunction() == PartyFunction.CN
      || p.partyFunction() == PartyFunction.DDS;
    var consignees = nullSafeStream(documentParties).filter(isConsigneeOrCFF).toList();
    verifyConsistency(consignees.size() == 1,
      "The B/L did not have exactly one consignee (documentParties[*].partyFunction in {OS, DDS})"
    );
  }

  private void validateNegotiableBL() {
    // We cannot tell the cases from each other current.
    verifyConsistency(false,
      "Negotiable B/Ls are currently not supported"
    );
  }


  private void validateShipper() {
    // TODO: Does DDR (shipper freight forwarder) also count?
    // NB: Currently if both are present, we do not know which of them is the "shipper" consignee
    Predicate<DocumentPartyTO> isShipperOrSFF = p -> p.partyFunction() == PartyFunction.OS
      || p.partyFunction() == PartyFunction.DDR;
    var shippers = nullSafeStream(documentParties).filter(isShipperOrSFF).toList();
    verifyConsistency(shippers.size() == 1,
      "The B/L did not have exactly one shipper (documentParties[*].partyFunction in {OS, DDR})"
    );
  }

  private void validateShippedOnBoardVsReceivedForShipment() {
    verifyConsistency(receivedForShipmentDate != null ^ shippedOnBoardDate != null,
      "The B/L must have exactly one of shippedOnBoardDate or shippedOnBoardDate");
    if (shippedOnBoardDate != null) {
      verifyConsistency(!shippedOnBoardDate.isBefore(issueDate),
        "The shippedOnBoardDate is after the issuance date which should not be possible"
      );
    } else {
      verifyConsistency(!receivedForShipmentDate.isBefore(issueDate),
        "The receivedForShipmentDate is after the issuance date which should not be possible"
      );
    }
  }

  private void verifyConsistency(boolean condition, String msg) {
    if (!condition) {
      throw new IllegalStateException(msg);
    }
  }

  private <T> Stream<T> nullSafeStream(Collection<T> c) {
    return c != null ? c.stream() : Stream.of();
  }
}
