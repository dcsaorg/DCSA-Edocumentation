package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

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
  @Enumerated(EnumType.STRING)
  private IncoTerms incoTerms;

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
  @Enumerated(EnumType.STRING)
  private CommunicationChannelCode communicationChannelCode;

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
