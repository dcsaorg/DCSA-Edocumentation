package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.edocumentation.domain.dfa.AbstractStateMachine;
import org.dcsa.edocumentation.domain.dfa.CannotLeaveTerminalStateException;
import org.dcsa.edocumentation.domain.dfa.DFA;
import org.dcsa.edocumentation.domain.dfa.DFADefinition;
import org.dcsa.edocumentation.domain.dfa.TargetStateIsNotSuccessorException;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CargoMovementType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CommunicationChannelCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.IncoTerms;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PaymentTerm;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReceiptDeliveryType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CANC;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CMPL;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.CONF;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.PENC;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.PENU;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.RECE;
import static org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus.REJE;

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
      @NamedAttributeNode("commodities"),
      @NamedAttributeNode("valueAddedServiceRequests"),
      @NamedAttributeNode("references"),
      @NamedAttributeNode("requestedEquipments"),
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
          attributeNodes = {@NamedAttributeNode("partyContactDetails")})
    })
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "booking")
public class Booking extends AbstractStateMachine<BkgDocumentStatus> {

  private static final DFADefinition<BkgDocumentStatus> BOOKING_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(PENU).successorNodes(REJE, CANC, PENU, PENC)
    .nonTerminalState(PENC).successorNodes(REJE, CANC, PENU, PENC, CONF)
    .nonTerminalState(CONF).successorNodes(REJE, CMPL, PENU, PENC)
    .terminalStates(CANC, CMPL, REJE)
    .build();

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "carrier_booking_request_reference", length = 100)
  private String carrierBookingRequestReference;

  @Column(name = "document_status")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private BkgDocumentStatus documentStatus = BkgDocumentStatus.RECE;

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
  private Set<ValueAddedServiceRequest> valueAddedServiceRequests;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "booking")
  private Set<RequestedEquipment> requestedEquipments;

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


  /**
   * Transition the booking into its {@link BkgDocumentStatus#CANC} state.
   */
  public void cancel() {
    transitionTo(CANC);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#REJE} state.
   */
  public void reject() {
    transitionTo(REJE);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENU} state.
   */
  public void pendingUpdate() {
    transitionTo(PENU);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public void pendingConfirmation() {
    transitionTo(PENC);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#PENC} state.
   */
  public void confirm() {
    transitionTo(CONF);
  }

  /**
   * Transition the booking into its {@link BkgDocumentStatus#CMPL} state.
   */
  public void complete() {
    transitionTo(CMPL);
  }

  @Transient
  private DFA<BkgDocumentStatus> dfa;

  public void setDocumentStatus(BkgDocumentStatus documentStatus) {
    this.documentStatus = documentStatus;
    this.dfa = null;
  }

  protected DFA<BkgDocumentStatus> getDfa() {
    if (dfa == null) {
      // Lazily generate via getter - the `setDocumentStatus` is not called during
      // the Builder's build method.
      dfa = BOOKING_DFA_DEFINITION.resumeFromState(documentStatus);
    }
    return dfa;
  }

  @Override
  protected void transitionTo(BkgDocumentStatus state) {
    super.transitionTo(state);
    this.documentStatus = state;
    this.bookingRequestUpdatedDateTime = OffsetDateTime.now();
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
