package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.dfa.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

@EqualsAndHashCode(callSuper = true)
@NamedEntityGraph(
  name = "graph.shippingInstructionSummary",

  attributeNodes = {
    @NamedAttributeNode(
      value = "consignmentItems",
      subgraph = "graph.shippingInstructionSummary.consignmentItem"
    ),
    @NamedAttributeNode("displayedNameForPlaceOfReceipt"),
    @NamedAttributeNode("displayedNameForPortOfLoad"),
    @NamedAttributeNode("displayedNameForPortOfDischarge"),
    @NamedAttributeNode("displayedNameForPlaceOfDelivery")
  },
  subgraphs = {
    @NamedSubgraph(
      name = "graph.shippingInstructionSummary.consignmentItem",
      attributeNodes = {
        @NamedAttributeNode("shipment")
      })
  }
)
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipping_instruction")
public class ShippingInstruction extends AbstractStateMachine<EblDocumentStatus>
    implements Persistable<UUID> {

  private static final DFADefinition<EblDocumentStatus> DEFAULT_EBL_DFA_DEFINITION =
      DFADefinition.builder(RECE)
          .nonTerminalState(RECE)
          .successorNodes(PENU, DRFT)
          .nonTerminalState(PENU)
          .successorNodes(RECE)
          .nonTerminalState(DRFT)
          .successorNodes(PENA, APPR)
          .nonTerminalState(PENA)
          .successorNodes(DRFT, APPR)
          .nonTerminalState(APPR)
          .successorNodes(ISSU)
          .nonTerminalState(ISSU)
          .successorNodes(SURR)
          .nonTerminalState(SURR)
          .successorNodes(VOID)
          .terminalStates(VOID)
          .build();

  private static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION =
      DFADefinition.builder(RECE)
          .nonTerminalState(RECE)
          .successorNodes(DRFT)
          .nonTerminalState(DRFT)
          .successorNodes(APPR)
          .nonTerminalState(APPR)
          .successorNodes(ISSU)
          .nonTerminalState(ISSU)
          .successorNodes(SURR)
          .nonTerminalState(SURR)
          .successorNodes(VOID)
          .terminalStates(VOID)
          .unreachableStates(PENU, PENA)
          .build();

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "shipping_instruction_reference")
  private String shippingInstructionReference;

  @Enumerated(EnumType.STRING)
  @Column(name = "document_status")
  private EblDocumentStatus documentStatus;

  @Column(name = "created_date_time")
  private OffsetDateTime shippingInstructionCreatedDateTime;

  @Column(name = "updated_date_time")
  private OffsetDateTime shippingInstructionUpdatedDateTime;

  @Column(name = "is_shipped_onboard_type")
  private Boolean isShippedOnBoardType;

  @Column(name = "number_of_copies_with_charges")
  private Integer numberOfCopiesWithCharges;

  @Column(name = "number_of_copies_without_charges")
  private Integer numberOfCopiesWithoutCharges;

  @Column(name = "number_of_originals_with_charges")
  private Integer numberOfOriginalsWithCharges;

  @Column(name = "number_of_originals_without_charges")
  private Integer numberOfOriginalsWithoutCharges;

  @Column(name = "is_electronic")
  private Boolean isElectronic;

  @Column(name = "is_to_order")
  private Boolean isToOrder;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  private Location placeOfIssue;

  @Enumerated(EnumType.STRING)
  @Column(name = "transport_document_type_code")
  private TransportDocumentTypeCode transportDocumentTypeCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "displayed_name_for_place_of_receipt")
  private DisplayedAddress displayedNameForPlaceOfReceipt;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "displayed_name_for_port_of_load")
  private DisplayedAddress displayedNameForPortOfLoad;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "displayed_name_for_port_of_discharge")
  private DisplayedAddress displayedNameForPortOfDischarge;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "displayed_name_for_place_of_delivery")
  private DisplayedAddress displayedNameForPlaceOfDelivery;

  @Column(name = "amendment_to_transport_document_id")
  private UUID amendmentToTransportDocument;

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shippingInstruction")
  private Set<ConsignmentItem> consignmentItems;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shippingInstructionID")
  private Set<DocumentParty> documentParties;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shippingInstructionID")
  private Set<Reference> references;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "shipping_instruction_id")
  private List<CustomsReference> customsReferences;

  // Todo consider if it makes sense to move this to a validation annotation
  public Boolean hasOnlyConfirmedBookings() {

    long unconfirmedBookingCount =
        this.consignmentItems.stream()
            .map(ConsignmentItem::getShipment)
            .map(Shipment::getBooking)
            .map(Booking::getDocumentStatus)
            .filter(status -> !status.equals(BkgDocumentStatus.CONF))
            .count();

    return unconfirmedBookingCount < 1;
  }

  // Todo consider if it makes sense to move this to a validation annotation
  public Boolean containsOneBooking() {
    long distinctBookingCount =
        this.consignmentItems.stream()
            .map(ConsignmentItem::getShipment)
            .map(Shipment::getBooking)
            .map(Booking::getCarrierBookingRequestReference)
            .distinct()
            .count();

    return distinctBookingCount == 1;
  }

  // certain characteristics like the transport plan, are share among all shipments in the shipping
  // instruction, so it is beneficial to be able to retrieve one
  public Shipment retrieveOneShipment() {
    return this.consignmentItems.stream()
        .map(ConsignmentItem::getShipment)
        .findAny()
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No shipment found in Shipping instruction with shipping instruction reference: "
                        + shippingInstructionReference));
  }

  @Transient private boolean isNew;

  public boolean isNew() {
    return id == null || isNew;
  }

  /** Transition the document into its {@link EblDocumentStatus#RECE} state. */
  public ShipmentEvent receive() {
    return processTransition(RECE, null);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable in the Amendment
   * flow.
   */
  public ShipmentEvent pendingUpdate(String reason) {
    return processTransition(PENU, reason);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when the EBL flow does
   * not support this state at all. I.e., calling {@link #pendingUpdate(String)} will trigger an
   * exception causing an internal server error status.
   */
  public boolean isPendingUpdateSupported() {
    return supportsState(PENU);
  }

  public void lockVersion(OffsetDateTime lockTime) {
    if (isNew()) {
      throw new IllegalStateException("Cannot lock a \"new\" version of the booking entity!");
    }
    this.validUntil = lockTime;
  }

  @Transient private DFA<EblDocumentStatus> dfa;

  protected DFADefinition<EblDocumentStatus> getDfaDefinition() {
    return this.amendmentToTransportDocument != null
        ? AMENDMENT_EBL_DFA_DEFINITION
        : DEFAULT_EBL_DFA_DEFINITION;
  }

  @Override
  protected EblDocumentStatus getResumeFromState() {
    return this.documentStatus;
  }

  @Override
  protected void transitionTo(EblDocumentStatus state) {
    super.transitionTo(state);
    this.documentStatus = state;
    this.shippingInstructionUpdatedDateTime = OffsetDateTime.now();
  }

  // Re-defined to make it visible to TransportDocument (protected also works like "package-private",
  // so we can keep it "non-public").
  @Override
  protected boolean supportsState(EblDocumentStatus state) {
    return super.supportsState(state);
  }

  protected ShipmentEvent processTransition(EblDocumentStatus status, String reason) {
    return processTransition(status, reason, this::shipmentEventSHIBuilder);
  }

  protected <C extends ShipmentEvent, B extends ShipmentEvent.ShipmentEventBuilder<C, B>> ShipmentEvent processTransition(
    EblDocumentStatus status,
    String reason,
    Function<OffsetDateTime, ShipmentEvent.ShipmentEventBuilder<C, B>> eventBuilder
  ) {
    return processTransition(status, reason, OffsetDateTime.now(), eventBuilder);
  }

  protected <C extends ShipmentEvent, B extends ShipmentEvent.ShipmentEventBuilder<C, B>> ShipmentEvent processTransition(
      EblDocumentStatus status,
      String reason,
      OffsetDateTime updateTime,
      Function<OffsetDateTime, ShipmentEvent.ShipmentEventBuilder<C, B>> eventBuilder
      ) {
    if (validUntil != null) {
      throw ConcreteRequestErrorMessageException.conflict("Cannot change state of locked document (the SI is locked)", null);
    }
    transitionTo(status);
    this.documentStatus = status;
    if (this.shippingInstructionCreatedDateTime == null) {
      this.shippingInstructionCreatedDateTime = updateTime;
      this.shippingInstructionUpdatedDateTime = updateTime;
    }
    if (id == null) {
      id = UUID.randomUUID();
      isNew = true;
    }
    if (shippingInstructionReference == null) {
      shippingInstructionReference = UUID.randomUUID().toString();
    }
    return eventBuilder.apply(updateTime)
        .shipmentEventTypeCode(status.asShipmentEventTypeCode())
        .reason(reason)
        .eventClassifierCode(EventClassifierCode.ACT)
        .eventDateTime(updateTime)
        .eventCreatedDateTime(updateTime)
        .build();
  }

  protected ShipmentEvent.ShipmentEventBuilder<?, ?> shipmentEventSHIBuilder(OffsetDateTime updateTime) {
    this.shippingInstructionUpdatedDateTime = updateTime;
    return ShipmentEvent.builder()
      .documentID(id)
      .documentReference(shippingInstructionReference)
      .documentTypeCode(DocumentTypeCode.SHI);
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(
      EblDocumentStatus currentState,
      EblDocumentStatus successorState,
      CannotLeaveTerminalStateException e) {
    // Special-case for terminal states, where we can generate quite nice sounding
    // messages such as "... because the document is void (VOID)".
    return ConcreteRequestErrorMessageException.conflict(
        "Cannot perform the requested action on the document as the document is "
            + currentState.getValue().toLowerCase()
            + " ("
            + currentState.name()
            + ")",
        e);
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(
      EblDocumentStatus currentState,
      EblDocumentStatus successorState,
      TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
        "It is not possible to perform the requested action on the booking with documentStatus ("
            + currentState.name()
            + ").",
        e);
  }
}
