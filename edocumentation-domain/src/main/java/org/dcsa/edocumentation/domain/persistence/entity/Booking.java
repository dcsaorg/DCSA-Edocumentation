package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.*;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.*;

@NamedEntityGraph(
    name = "graph.booking-summary",
    attributeNodes = {@NamedAttributeNode("vessel"), @NamedAttributeNode("modeOfTransport")})
@NamedEntityGraph(
    name = "graph.booking",
    attributeNodes = {
      @NamedAttributeNode("vessel"),
      @NamedAttributeNode("modeOfTransport"),
      @NamedAttributeNode("placeOfIssue"),
      @NamedAttributeNode("invoicePayableAt"),
      @NamedAttributeNode(value = "commodities", subgraph = "graph.commodities"),
      @NamedAttributeNode("references"),
      @NamedAttributeNode(value = "documentParties", subgraph = "graph.documentParties"),
      @NamedAttributeNode("shipmentLocations")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "graph.documentParties",
          attributeNodes = {
            @NamedAttributeNode("displayedAddress"),
            @NamedAttributeNode(value = "party", subgraph = "graph.party")
          }),
      @NamedSubgraph(
          name = "graph.party",
          attributeNodes = {@NamedAttributeNode("partyContactDetails")}),
      @NamedSubgraph(
        name = "graph.commodities",
        attributeNodes = {
          @NamedAttributeNode("requestedEquipments")
        })
    })
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
public class Booking extends AbstractStateMachine<BkgDocumentStatus> implements Persistable<UUID> {


  private static final Set<BkgDocumentStatus> CAN_BE_VALIDATED =
    Set.of(BkgDocumentStatus.RECE, BkgDocumentStatus.PENC);
  private static final Predicate<String> IS_SOURCE_LOCATION_TYPE = Set.of(LocationType.PRE.name(), LocationType.POL.name())::contains;
  private static final Predicate<String> IS_DESTINATION_LOCATION_TYPE = Set.of(LocationType.POD.name(), LocationType.PDE.name())::contains;
  private static final Predicate<String> IS_SOURCE_OR_DESTINATION_LOCATION_TYPE =
    IS_SOURCE_LOCATION_TYPE.or(IS_DESTINATION_LOCATION_TYPE);
  private static final Predicate<ShipmentLocation> IS_SOURCE_OR_DESTINATION_LOCATION =
    sl -> IS_SOURCE_OR_DESTINATION_LOCATION_TYPE.test(sl.getShipmentLocationTypeCode());
  private static Predicate<ShipmentLocation> HAS_ADDRESS = sl -> sl.getLocation().getAddress() != null;
  private static Predicate<ShipmentLocation> HAS_UNLOCATION_CODE = sl -> sl.getLocation().getUNLocationCode() != null;


  private static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "carrier_booking_request_reference", length = 100)
  private String carrierBookingRequestReference;

  @Column(name = "document_status")
  @Enumerated(EnumType.STRING)
  private BkgDocumentStatus documentStatus;

  @Column(name = "receipt_type_at_origin")
  @Enumerated(EnumType.STRING)
  private ReceiptDeliveryType receiptTypeAtOrigin;

  @Column(name = "delivery_type_at_destination")
  @Enumerated(EnumType.STRING)
  private ReceiptDeliveryType deliveryTypeAtDestination;

  @Column(name = "cargo_movement_type_at_origin")
  @Enumerated(EnumType.STRING)
  private CargoMovementType cargoMovementTypeAtOrigin;

  @Column(name = "cargo_movement_type_at_destination")
  @Enumerated(EnumType.STRING)
  private CargoMovementType cargoMovementTypeAtDestination;

  @Column(name = "booking_request_datetime")
  private OffsetDateTime bookingRequestCreatedDateTime;

  @Column(name = "service_contract_reference", length = 30)
  private String serviceContractReference;

  @Column(name = "payment_term_code")
  @PseudoEnum(value = "paymentterms.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String paymentTermCode;

  @Column(name = "is_partial_load_allowed")
  private Boolean isPartialLoadAllowed;

  @Column(name = "is_export_declaration_required")
  private Boolean isExportDeclarationRequired;

  @Column(name = "export_declaration_reference", length = 35)
  private String exportDeclarationReference;

  @Column(name = "is_import_license_required")
  private Boolean isImportLicenseRequired;

  @Column(name = "import_license_reference", length = 35)
  private String importLicenseReference;

  @Column(name = "is_ams_aci_filing_required")
  private Boolean isAMSACIFilingRequired;

  @Column(name = "is_destination_filing_required")
  private Boolean isDestinationFilingRequired;

  @Column(name = "contract_quotation_reference", length = 35)
  private String contractQuotationReference;

  @Column(name = "incoterms")
  @PseudoEnum(value = "incotermscodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String incoTerms;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "invoice_payable_at_id")
  private Location invoicePayableAt;

  @Column(name = "expected_departure_date")
  private LocalDate expectedDepartureDate;

  @Column(name = "expected_arrival_at_place_of_delivery_start_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryStartDate;

  @Column(name = "expected_arrival_at_place_of_delivery_end_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryEndDate;

  @Column(name = "transport_document_type_code")
  @Enumerated(EnumType.STRING)
  private TransportDocumentTypeCode transportDocumentTypeCode;

  @Column(name = "transport_document_reference", length = 20)
  private String transportDocumentReference;

  @Column(name = "booking_channel_reference", length = 20)
  private String bookingChannelReference;

  @Column(name = "communication_channel_code")
  @PseudoEnum(value = "communicationchannelqualifier.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String communicationChannelCode;

  @Column(name = "is_equipment_substitution_allowed")
  private Boolean isEquipmentSubstitutionAllowed;

  @Column(name = "declared_value_currency_code", length = 3)
  private String declaredValueCurrency;

  @Column(name = "declared_value")
  private Float declaredValue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "pre_carriage_mode_of_transport_code")
  private ModeOfTransport modeOfTransport;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vessel_id")
  private Vessel vessel;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  private Location placeOfIssue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<Commodity> commodities;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<RequestedEquipmentGroup> requestedEquipments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  @Valid
  private Set<DocumentParty> documentParties;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<ShipmentLocation> shipmentLocations;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "voyage_id")
  private Voyage voyage;

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  // updatedDateTime is metadata to avoid having to query shipment_event for updated dateTime.
  // This is not part of the official IM model. They are added in the sql only.
  @Column(name = "updated_date_time")
  protected OffsetDateTime bookingRequestUpdatedDateTime;

  @Transient
  private boolean isNew;

  public boolean isNew() {
    return id == null || isNew;
  }


  /**
   * Subject to change. Reefer will probably change it.
   */
  public ValidationResult<BkgDocumentStatus> asyncValidation(Validator validator) {
    LocalDate today = LocalDate.now();
    List<String> validationErrors = new ArrayList<>();


    if (!CAN_BE_VALIDATED.contains(documentStatus)) {
      throw new IllegalStateException("documentStatus must be one of " + CAN_BE_VALIDATED);
    }

    if (isInThePast(today, expectedDepartureDate)) {
      validationErrors.add("expectedDepartureDate is in the past");
    }
    if (isInThePast(today, expectedArrivalAtPlaceOfDeliveryStartDate)) {
      validationErrors.add("expectedArrivalAtPlaceOfDeliveryStartDate is in the past");
    }
    if (isInThePast(today, expectedArrivalAtPlaceOfDeliveryEndDate)) {
      validationErrors.add("expectedArrivalAtPlaceOfDeliveryEndDate is in the past");
    }
    validateShipmentLocations(shipmentLocations, validationErrors);
    for (var violation : validator.validate(this, AsyncShipperProvidedDataValidation.class)) {
      validationErrors.add(violation.getPropertyPath().toString() + ": " +  violation.getMessage());
    }
    var proposedStatus = validationErrors.isEmpty()
      ? PENC
      : PENU
      ;
    return new ValidationResult<>(proposedStatus, validationErrors);
  }

  private boolean isInThePast(LocalDate today, LocalDate time) {
    return time != null && today.isAfter(time);
  }

  private void validateShipmentLocations(Set<ShipmentLocation> shipmentLocations, List<String> validationErrors) {
    if (shipmentLocations.isEmpty()) {
      validationErrors.add("Invalid booking: Shipment locations should not be empty");
    }

    boolean slHasPREorPOLs =
      shipmentLocations.stream()
        .map(ShipmentLocation::getShipmentLocationTypeCode)
        .anyMatch(IS_SOURCE_LOCATION_TYPE);

    if (!slHasPREorPOLs) {
      validationErrors.add("No ShipmentLocationTypeCode of PRE or POL found in the shipmentLocations."
        + " At least one of them should be provided.");
    }

    boolean slHasPODorPDE =
      shipmentLocations.stream()
        .map(ShipmentLocation::getShipmentLocationTypeCode)
        .anyMatch(IS_DESTINATION_LOCATION_TYPE);

    if (!slHasPODorPDE) {
      validationErrors.add("No ShipmentLocationTypeCode of POD or PDE found in the shipmentLocations."
        + "At least one of them should be provided.");
    }


    var filteredByUNLocationCode =
      shipmentLocations.stream()
        .filter(IS_SOURCE_OR_DESTINATION_LOCATION.and(HAS_UNLOCATION_CODE))
        .toList();

    var filteredByUNLocationCodeCount = filteredByUNLocationCode.size();

    boolean hasUniqueUNLocationCodes =
      filteredByUNLocationCode.stream()
        .map(sl -> sl.getLocation().getUNLocationCode())
        .distinct()
        .count()
        == filteredByUNLocationCodeCount;

    if (!hasUniqueUNLocationCodes) {
      validationErrors.add("Duplicate UNLocationCodes found in shipmentLocations");
    }

    var filteredByAddress =
      shipmentLocations.stream()
        .filter(IS_SOURCE_OR_DESTINATION_LOCATION.and(HAS_ADDRESS))
        .toList();

    var filteredByAddressCount = filteredByAddress.size();

    boolean hasUniqueAddresses =
      shipmentLocations.stream()
        .filter(IS_SOURCE_OR_DESTINATION_LOCATION.and(HAS_ADDRESS))
        .map(sl -> sl.getLocation().getAddress())
        .filter(
          address ->
            filteredByAddress.stream()
              .noneMatch(other -> other.getLocation().getAddress().equals(address)))
        .count()
        == filteredByAddressCount;

    if (!hasUniqueAddresses) {
      validationErrors.add("Duplicate addresses found in shipmentLocations");
    }
  }


  /**
   * Transition the booking into its {@link BkgDocumentStatus#RECE} state.
   */
  public ShipmentEvent receive() {
    return processTransition(RECE, null);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CANC} state.
   */
  public ShipmentEvent cancel(String reason, OffsetDateTime updateTime) {
    return processTransition(CANC, reason, updateTime);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#REJE} state.
   */
  public ShipmentEvent reject(String reason) {
    return processTransition(REJE, reason);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENU} state.
   */
  public ShipmentEvent pendingUpdate(String reason, OffsetDateTime updateTime) {
    return processTransition(PENU, reason, updateTime);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public ShipmentEvent pendingConfirmation(String reason, OffsetDateTime updateTime) {
    return processTransition(PENC, reason, updateTime);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public ShipmentEvent confirm(OffsetDateTime confirmationTime) {
    return processTransition(CONF, null, confirmationTime);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CMPL} state.
   */
  public ShipmentEvent complete() {
    return processTransition(CMPL, null);
  }

  public void lockVersion(OffsetDateTime lockTime) {
    if (isNew()) {
      throw new IllegalStateException("Cannot lock a \"new\" version of the booking entity!");
    }
    this.validUntil = lockTime;
  }

  @Override
  protected DFADefinition<BkgDocumentStatus> getDfaDefinition() {
    return BOOKING_DFA_DEFINITION;
  }

  @Override
  protected BkgDocumentStatus getResumeFromState() {
    return this.documentStatus;
  }

  protected ShipmentEvent processTransition(BkgDocumentStatus status, String reason) {
    return processTransition(status, reason, OffsetDateTime.now());
  }

  protected ShipmentEvent processTransition(BkgDocumentStatus status, String reason, OffsetDateTime updateTime) {
    if (this.validUntil != null) {
      throw new IllegalStateException("Cannot change state on a frozen version!");
    }
    transitionTo(status);
    this.documentStatus = status;
    this.bookingRequestUpdatedDateTime = updateTime;
    if (this.bookingRequestCreatedDateTime == null) {
      this.bookingRequestCreatedDateTime = updateTime;
    }
    if (id == null) {
      id = UUID.randomUUID();
      isNew = true;
    }
    if (carrierBookingRequestReference == null) {
      carrierBookingRequestReference = UUID.randomUUID().toString();
    }
    return ShipmentEvent.builder()
      .documentID(id)
      .documentReference(carrierBookingRequestReference)
      .documentTypeCode(DocumentTypeCode.CBR)
      .shipmentEventTypeCode(status.asShipmentEventTypeCode())
      .reason(reason)
      .eventClassifierCode(EventClassifierCode.ACT)
      .eventDateTime(updateTime)
      .eventCreatedDateTime(updateTime)
      .build();
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(BkgDocumentStatus currentState, BkgDocumentStatus successorState, CannotLeaveTerminalStateException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the booking because the booking is "
        + currentState.getValue().toLowerCase() + " (" + currentState.name() + ")",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(BkgDocumentStatus currentState, BkgDocumentStatus successorState, TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with documentStatus ("
        + currentState.name() + ").",
      e
    );
  }
}
