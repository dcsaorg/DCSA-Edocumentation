package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.validations.*;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import static org.dcsa.edocumentation.infra.enums.BookingStatus.*;
import org.dcsa.edocumentation.infra.validation.StringEnumValidation;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

@NamedEntityGraph(
    name = "graph.booking-summary",
    attributeNodes = {@NamedAttributeNode("vessel")})
@NamedEntityGraph(
    name = "graph.booking",
    attributeNodes = {
      @NamedAttributeNode("vessel"),
      @NamedAttributeNode("placeOfIssue"),
      @NamedAttributeNode("invoicePayableAt"),
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
    })
@Slf4j
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
@BookingValidation(groups = AsyncShipperProvidedDataValidation.class)
public class Booking extends AbstractStateMachine<String> implements Persistable<UUID> {

  private static final Set<String> CAN_BE_VALIDATED = Set.of(RECEIVED,
    PENDING_UPDATES_CONFIRMATION,
    PENDING_AMENDMENTS_APPROVAL);

  private static final DFADefinition<String> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECEIVED)
    .nonTerminalState(RECEIVED)
    .successorNodes(CONFIRMED, PENDING_UPDATE, REJECTED, // Carrier
      CANCELLED, PENDING_UPDATES_CONFIRMATION) // Shipper
    .nonTerminalState(PENDING_UPDATE)
    .successorNodes(PENDING_UPDATE, REJECTED, // Carrier
      PENDING_UPDATES_CONFIRMATION, CANCELLED) // Shipper
    .nonTerminalState(PENDING_UPDATES_CONFIRMATION)
    .successorNodes(CONFIRMED, PENDING_UPDATE, REJECTED, // Carrier
      CANCELLED) // Shipper
    .nonTerminalState(CONFIRMED)
    .successorNodes(PENDING_UPDATE, COMPLETED, DECLINED, // Carrier
      PENDING_AMENDMENTS_APPROVAL, CANCELLED) // Shipper
    .nonTerminalState(PENDING_AMENDMENTS_APPROVAL)
    .successorNodes(PENDING_UPDATE, CONFIRMED, DECLINED, // Carrier
      CANCELLED) // Shipper
    .terminalStates(COMPLETED, REJECTED, DECLINED, // Carrier
      CANCELLED) // Shipper
    .build();

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "carrier_booking_request_reference", length = 100)
  private String carrierBookingRequestReference;

  @Column(name = "booking_status")
  @StringEnumValidation(value= BookingStatus.class)
  private String bookingStatus;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id", referencedColumnName = "id", nullable = false)
  @OrderColumn(name = "element_order")
  private List<BookingRequestedChange> requestedChanges;

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
  @Enumerated(EnumType.STRING)
  private PaymentTerm paymentTermCode;

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
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location invoicePayableAt;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
  @Column(name = "expected_departure_date")
  private LocalDate expectedDepartureDate;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
  @Column(name = "expected_arrival_at_place_of_delivery_start_date")
  private LocalDate expectedArrivalAtPlaceOfDeliveryStartDate;

  @Future(groups = AsyncShipperProvidedDataValidation.class, message ="must be in the future" )
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

  @PseudoEnum(value = "currencycodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  @Column(name = "declared_value_currency_code", length = 3)
  private String declaredValueCurrency;

  @Column(name = "declared_value")
  private Float declaredValue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "vessel_id")
  private Vessel vessel;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location placeOfIssue;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
  @OrderColumn(name = "list_order")
  private List<RequestedEquipmentGroup> requestedEquipments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<@Valid DocumentParty> documentParties;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  @Size(min = 2, message = "Must have at least two shipment locations", groups = AsyncShipperProvidedDataValidation.class)
  @NotNull(groups = AsyncShipperProvidedDataValidation.class)
  private Set<@Valid ShipmentLocation> shipmentLocations;

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
  public ValidationResult<String> asyncValidation(Validator validator) {
    List<String> validationErrors = new ArrayList<>();

    if (!CAN_BE_VALIDATED.contains(bookingStatus)) {
      throw new IllegalStateException("bookingStatus must be one of " + CAN_BE_VALIDATED);
    }
    if (this.requestedChanges == null) {
      this.requestedChanges = new ArrayList<>();
    }
    clearRequestedChanges();

    for (var violation : validator.validate(this, AsyncShipperProvidedDataValidation.class)) {
      this.requestedChanges.add(BookingRequestedChange.fromConstraintViolation(violation));
      validationErrors.add(violation.getPropertyPath().toString() + ": " +  violation.getMessage());
    }

    // TODO: according to the latest Booking State Transition Diagram (STD),
    //  PENDING_UPDATES_CONFIRMATION should be replaced with CONFIRMED, but this change should be done together
    // with other STD-related changes so that new BOOKING_DFA_DEFINITION does not get broken
    var proposedStatus = validationErrors.isEmpty() ? PENDING_UPDATES_CONFIRMATION : PENDING_UPDATE;

    return new ValidationResult<>(proposedStatus, validationErrors);
  }

  private void clearRequestedChanges() {
    if (this.requestedChanges != null && !this.requestedChanges.isEmpty()) {
      this.requestedChanges.clear();
    }
  }


  /**
   * Transition the booking into its {@link BookingStatus#RECEIVED} state.
   */
  public void receive() {
    processTransition(RECEIVED, null, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#CANCELLED} state.
   */
  public void cancel(String reason, OffsetDateTime updateTime) {
    processTransition(CANCELLED, reason, updateTime, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#REJECTED} state.
   */
  public void reject(String reason) {
    processTransition(REJECTED, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#REJECTED} state.
   */
  public void decline(String reason) {
    processTransition(DECLINED, reason, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_UPDATE} state.
   */
  public void pendingUpdate(String reason, OffsetDateTime updateTime) {
    processTransition(PENDING_UPDATE, reason, updateTime, false);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_UPDATES_CONFIRMATION} state.
   */
  public void pendingUpdatesConfirmation(String reason, OffsetDateTime updateTime) {
    processTransition(PENDING_UPDATES_CONFIRMATION, reason, updateTime, true);
  }

  /**
   * Transition the booking into its {@link BookingStatus#PENDING_AMENDMENTS_APPROVAL} state.
   */
  public void pendingAmendmentsApproval(String reason, OffsetDateTime updateTime) {
    processTransition(PENDING_AMENDMENTS_APPROVAL, reason, updateTime, true);
  }

  /**
   * Transition the booking into its {@link BookingStatus#CONFIRMED} state.
   */
  public void confirm(OffsetDateTime confirmationTime) {
    processTransition(CONFIRMED, null, confirmationTime, true);
  }

  /**
   * Transition the booking into its {@link BookingStatus#COMPLETED} state.
   */
  public void complete() {
    processTransition(COMPLETED, null, true);
  }

  public void lockVersion(OffsetDateTime lockTime) {
    if (isNew()) {
      throw new IllegalStateException("Cannot lock a \"new\" version of the booking entity!");
    }
    this.validUntil = lockTime;
  }

  @Override
  protected DFADefinition<String> getDfaDefinition() {
    return BOOKING_DFA_DEFINITION;
  }

  @Override
  protected String getResumeFromState() {
    return this.bookingStatus;
  }

  protected void processTransition(String bookingStatus, String reason, boolean clearRequestedChanges) {
    processTransition(bookingStatus, reason, OffsetDateTime.now(), clearRequestedChanges);
  }

  protected void processTransition(String bookingStatus, String reason, OffsetDateTime updateTime, boolean clearRequestedChanges) {
    if (this.validUntil != null) {
      throw new IllegalStateException("Cannot change state on a frozen version!");
    }
    transitionTo(bookingStatus);
    this.bookingStatus = bookingStatus;
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
    if (clearRequestedChanges) {
      this.clearRequestedChanges();
    }
  }

  @Override
  protected RuntimeException errorForAttemptToLeaveTerminalState(String currentState,
                                                                 String successorState,
                                                                 CannotLeaveTerminalStateException e) {
    log.error("Booking with id=" + (id != null ? id.toString() : "null") +" is in terminal state " + currentState +
      ", can not transition to state " + successorState);
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the booking because the booking status is '"
        + currentState + "'",
      e
    );
  }

  @Override

  protected RuntimeException errorForTargetStateNotListedAsSuccessor(String currentState,
                                                                     String successorState,
                                                                     TargetStateIsNotSuccessorException e) {
    log.error("Booking with id=" + (id != null ? id.toString() : "null") +" is in state " + currentState +
      ", can not transition to unexpected state " + successorState);
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with the booking status '"
        + currentState + "'",
      e
    );
  }

  public void assignRequestedEquipment(List<RequestedEquipmentGroup> requestedEquipments) {
    this.requestedEquipments = requestedEquipments;
    if (requestedEquipments != null) {
      for (var re : requestedEquipments) {
        re.setBooking(this);
      }
    }
  }
}
