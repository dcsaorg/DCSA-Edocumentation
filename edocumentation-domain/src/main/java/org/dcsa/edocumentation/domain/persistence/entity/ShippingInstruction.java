package org.dcsa.edocumentation.domain.persistence.entity;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;
import org.dcsa.edocumentation.domain.dfa.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.validations.*;
import org.dcsa.edocumentation.domain.validations.LocationSubType;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Persistable;

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
@ShippingInstructionValidation(groups = AsyncShipperProvidedDataValidation.class)
public class ShippingInstruction extends AbstractStateMachine<EblDocumentStatus>
    implements Persistable<UUID> {

  private static final Set<EblDocumentStatus> CAN_BE_VALIDATED = Set.of(EblDocumentStatus.RECE);

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
          .unreachableStates(REJE)
          .build();

  private static final DFADefinition<EblDocumentStatus> AMENDMENT_EBL_DFA_DEFINITION =
      DFADefinition.builder(RECE)
          .nonTerminalState(RECE)
          .successorNodes(DRFT, REJE)
          .nonTerminalState(DRFT)
          .successorNodes(APPR)
          .nonTerminalState(APPR)
          .successorNodes(ISSU)
          .nonTerminalState(ISSU)
          .successorNodes(SURR)
          .nonTerminalState(SURR)
          .successorNodes(VOID)
          .terminalStates(REJE, VOID)
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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "shipping_instruction_id", referencedColumnName = "id", nullable = false)
  @OrderColumn(name = "element_order")
  private List<ShippingInstructionRequestedChange> requestedChanges;

  @Column(name = "created_date_time")
  private OffsetDateTime shippingInstructionCreatedDateTime;

  @Column(name = "updated_date_time")
  private OffsetDateTime shippingInstructionUpdatedDateTime;

  @Column(name = "is_shipped_onboard_type")
  private Boolean isShippedOnBoardType;

  @Column(name = "number_of_copies_with_charges")
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  private Integer numberOfCopiesWithCharges;

  @Column(name = "number_of_copies_without_charges")
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  private Integer numberOfCopiesWithoutCharges;

  @Column(name = "number_of_originals_with_charges")
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  private Integer numberOfOriginalsWithCharges;

  @Column(name = "number_of_originals_without_charges")
  @NotNull(groups = PaperBLValidation.class, message = "Must not be null for a paper B/L")
  @Null(groups = EBLValidation.class, message = "Must be omitted for an eBL")
  private Integer numberOfOriginalsWithoutCharges;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "invoice_payable_at_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location invoicePayableAt;

  @Column(name = "is_electronic")
  private Boolean isElectronic;

  @Column(name = "is_to_order")
  private Boolean isToOrder;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
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
  @OrderColumn(name = "si_entry_order")
  private List<@Valid ConsignmentItem> consignmentItems;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shippingInstructionID")
  private Set<@Valid DocumentParty> documentParties;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shippingInstructionID")
  private Set<Reference> references;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "shipping_instruction_id")
  private List<CustomsReference> customsReferences;

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

  public ValidationResult<EblDocumentStatus> asyncValidation(Validator validator) {
    List<String> validationErrors = new ArrayList<>();

    if (!CAN_BE_VALIDATED.contains(documentStatus)) {
      throw new IllegalStateException("documentStatus must be one of " + CAN_BE_VALIDATED);
    }
    if (this.requestedChanges == null) {
      this.requestedChanges = new ArrayList<>();
    }
    clearRequestedChanges();
    Class<?>[] validations = {
      AsyncShipperProvidedDataValidation.class,
      (this.isElectronic == Boolean.TRUE ? EBLValidation.class : PaperBLValidation.class),
    };
    for (var violation : validator.validate(this, validations)) {
      validationErrors.add(violation.getPropertyPath().toString() + ": " +  violation.getMessage());
      this.requestedChanges.add(ShippingInstructionRequestedChange.fromConstraintViolation(violation));
    }

    var proposedStatus = validationErrors.isEmpty()
      ? DRFT
      : (amendmentToTransportDocument != null ? REJE : PENU)
      ;
    return new ValidationResult<>(proposedStatus, validationErrors);
  }


  private void clearRequestedChanges() {
    if (this.requestedChanges != null && !this.requestedChanges.isEmpty()) {
      this.requestedChanges.clear();
    }
  }



  /** Transition the document into its {@link EblDocumentStatus#RECE} state. */
  public void receive() {
    processTransition(RECE, true);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable in the Amendment
   * flow.
   */
  public void pendingUpdate() {
    processTransition(PENU, false);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENU} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when the EBL flow does
   * not support this state at all. I.e., calling {@link #pendingUpdate()} will trigger an
   * exception causing an internal server error status.
   */
  public boolean isPendingUpdateSupported() {
    return supportsState(PENU);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#REJE} state.
   *
   * <p>This state is only reachable in the Amendment flow.
   */
  public void rejected(String reason) {
    processTransition(REJE, false);
    this.clearRequestedChanges();
    this.addRequestedChanges(ShippingInstructionRequestedChange.builder().message(reason).build());
  }

  private void addRequestedChanges(ShippingInstructionRequestedChange change) {
    if (this.requestedChanges == null) {
      this.requestedChanges = new ArrayList<>();
    }
    this.requestedChanges.add(change);
  }

  public void lockVersion(OffsetDateTime lockTime) {
    if (isNew()) {
      throw new IllegalStateException("Cannot lock a \"new\" version of the booking entity!");
    }
    this.validUntil = lockTime;
  }

  protected DFADefinition<EblDocumentStatus> getDfaDefinition() {
    return this.amendmentToTransportDocument != null
        ? AMENDMENT_EBL_DFA_DEFINITION
        : DEFAULT_EBL_DFA_DEFINITION;
  }

  @Override
  protected EblDocumentStatus getResumeFromState() {
    return this.documentStatus;
  }

  protected EblDocumentStatus getCurrentState() {
    var d = this.getDfa();
    if (d == null) {
      return this.getResumeFromState();
    }
    return d.getCurrentState();
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

  protected void processTransition(
    EblDocumentStatus status,
    boolean clearRequestedChanges
  ) {
    processTransition(status, OffsetDateTime.now(), clearRequestedChanges);
  }

  protected void processTransition(
      EblDocumentStatus status,
      OffsetDateTime updateTime,
      boolean clearRequestedChanges
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
    if (clearRequestedChanges) {
      clearRequestedChanges();
    }
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
