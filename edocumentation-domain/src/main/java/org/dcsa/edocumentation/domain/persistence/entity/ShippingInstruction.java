package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.dfa.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

@NamedEntityGraph(
    name = "graph.shipping-instruction-summary",
    attributeNodes = {@NamedAttributeNode("consignmentItems")})
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipping_instruction")
public class ShippingInstruction extends AbstractStateMachine<EblDocumentStatus> implements Persistable<UUID> {


  private static final DFADefinition<EblDocumentStatus> DEFAULT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(PENU, DRFT)
    .nonTerminalState(PENU).successorNodes(RECE)
    .nonTerminalState(DRFT).successorNodes(PENA, APPR)
    .nonTerminalState(PENA).successorNodes(DRFT, APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
    .terminalStates(VOID)
    .build();

  private static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION = DFADefinition.builder(RECE)
    .nonTerminalState(RECE).successorNodes(DRFT)
    .nonTerminalState(DRFT).successorNodes(APPR)
    .nonTerminalState(APPR).successorNodes(ISSU)
    .nonTerminalState(ISSU).successorNodes(SURR)
    .nonTerminalState(SURR).successorNodes(VOID)
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

  @Column(name = "displayed_name_for_place_of_receipt")
  private String displayedNameForPlaceOfReceipt;

  @Column(name = "displayed_name_for_port_of_load")
  private String displayedNameForPortOfLoad;

  @Column(name = "displayed_name_for_port_of_discharge")
  private String displayedNameForPortOfDischarge;

  @Column(name = "displayed_name_for_place_of_delivery")
  private String displayedNameForPlaceOfDelivery;

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

  @Transient
  private boolean isNew;

  public boolean isNew() {
    return id == null || isNew;
  }


  /**
   * Transition the document into its {@link EblDocumentStatus#RECE} state.
   */
  public ShipmentEvent receive() {
    return processTransition(RECE, null, DocumentTypeCode.SHI);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  public ShipmentEvent pendingUpdate(String reason) {
    return processTransition(PENU, reason, DocumentTypeCode.SHI);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingUpdate(String)}
   * will trigger an exception causing an internal server error status.</p>
   */
  public boolean isPendingUpdateSupported() {
    return supportsState(PENU);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#DRFT} state.
   */
  public ShipmentEvent draft() {
    // TODO: Should this be moved to the TRD?
    return processTransition(DRFT, null, DocumentTypeCode.SHI);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable
   * in the Amendment flow.</p>
   */
  public ShipmentEvent pendingApproval(String reason) {
    // TODO: Should this be moved to the TRD?
    return processTransition(PENA, reason, DocumentTypeCode.SHI);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when
   * the EBL flow does not support this state at all. I.e., calling {@link #pendingApproval(String)}
   * will trigger an exception causing an internal server error status.</p>
   */
  public boolean isPendingApprovalSupported() {
    // TODO: Move to the TRD if pendingApproval moves.
    return supportsState(PENA);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#APPR} state.
   */
  public ShipmentEvent approve() {
    // TODO: Should this be moved to the TRD?
    return processTransition(APPR, null, DocumentTypeCode.SHI);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#ISSU} state.
   */
  public void issue() {
    // TODO: Should this be moved to the TRD?
    processTransition(ISSU, null, DocumentTypeCode.SHI);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#SURR} state.
   */
  public ShipmentEvent surrender() {
    // TODO: Should this be moved to the TRD?
    return processTransition(SURR, null, DocumentTypeCode.SHI);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#VOID} state.
   */
  // "void" is a keyword and cannot be used as a method name.
  public ShipmentEvent voidDocument() {
    // TODO: Should this be moved to the TRD?
    return processTransition(VOID, null, DocumentTypeCode.SHI);
  }

  public void lockVersion(OffsetDateTime lockTime) {
    if (isNew()) {
      throw new IllegalStateException("Cannot lock a \"new\" version of the booking entity!");
    }
    this.validUntil = lockTime;
  }


  @Transient
  private DFA<EblDocumentStatus> dfa;

  protected DFADefinition<EblDocumentStatus> getDfaDefinition() {
    return this.amendmentToTransportDocument != null ? AMENDMENT_EBL_DFA_DEFINITION : DEFAULT_EBL_DFA_DEFINITION;
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

  protected ShipmentEvent processTransition(EblDocumentStatus status, String reason, DocumentTypeCode documentTypeCode) {
    return processTransition(status, reason, documentTypeCode, OffsetDateTime.now());
  }

  protected ShipmentEvent processTransition(EblDocumentStatus status, String reason, DocumentTypeCode documentTypeCode, OffsetDateTime updateTime) {
    transitionTo(status);
    this.documentStatus = status;
    this.shippingInstructionUpdatedDateTime = updateTime;
    if (this.shippingInstructionCreatedDateTime == null) {
      this.shippingInstructionCreatedDateTime = updateTime;
    }
    if (id == null) {
      id = UUID.randomUUID();
      isNew = true;
    }
    if (shippingInstructionReference == null) {
      shippingInstructionReference = UUID.randomUUID().toString();
    }
    return ShipmentEvent.builder()
      .documentID(id)
      .documentReference(shippingInstructionReference)
      .documentTypeCode(documentTypeCode)
      .shipmentEventTypeCode(status.asShipmentEventTypeCode())
      .reason(reason)
      .eventClassifierCode(EventClassifierCode.ACT)
      .eventDateTime(updateTime)
      .eventCreatedDateTime(updateTime)
      .build();
  }

  @Override
  protected RuntimeException errorForAttemptLeavingToLeaveTerminalState(EblDocumentStatus currentState, EblDocumentStatus successorState, CannotLeaveTerminalStateException e) {
    // Special-case for terminal states, where we can generate quite nice sounding
    // messages such as "... because the document is void (VOID)".
    return ConcreteRequestErrorMessageException.conflict(
      "Cannot perform the requested action on the document as the document is "
        + currentState.getValue().toLowerCase() + " (" + currentState.name() + ")",
      e
    );
  }

  @Override
  protected RuntimeException errorForTargetStatNotListedAsSuccessor(EblDocumentStatus currentState, EblDocumentStatus successorState, TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
      "It is not possible to perform the requested action on the booking with documentStatus ("
        + currentState.name() + ").",
      e
    );
  }

}
