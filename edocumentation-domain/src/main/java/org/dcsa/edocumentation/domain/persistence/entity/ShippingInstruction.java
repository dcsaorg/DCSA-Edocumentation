package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.time.OffsetDateTime;
import java.util.*;
import lombok.*;
import org.dcsa.edocumentation.domain.dfa.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.*;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.validations.*;
import org.dcsa.edocumentation.domain.validations.LocationSubType;
import org.dcsa.edocumentation.infra.enums.EblDocumentStatus;
import org.dcsa.edocumentation.infra.validation.StringEnumValidation;
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
        @NamedAttributeNode("booking")
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
public class ShippingInstruction extends AbstractStateMachine<String>
    implements Persistable<UUID> {

  private static final Set<String> CAN_BE_VALIDATED = Set.of(EblDocumentStatus.RECEIVED);

  private static final DFADefinition<String> DEFAULT_EBL_DFA_DEFINITION =
      DFADefinition.builder(EblDocumentStatus.RECEIVED)
          .nonTerminalState(EblDocumentStatus.RECEIVED)
          .successorNodes(EblDocumentStatus.PENDING_UPDATE, EblDocumentStatus.DRAFT)
          .nonTerminalState(EblDocumentStatus.PENDING_UPDATE)
          .successorNodes(EblDocumentStatus.RECEIVED)
          .nonTerminalState(EblDocumentStatus.DRAFT)
          .successorNodes(EblDocumentStatus.PENDING_APPROVAL, EblDocumentStatus.APPROVED)
          .nonTerminalState(EblDocumentStatus.PENDING_APPROVAL)
          .successorNodes(EblDocumentStatus.DRAFT, EblDocumentStatus.APPROVED)
          .nonTerminalState(EblDocumentStatus.APPROVED)
          .successorNodes(EblDocumentStatus.ISSUED)
          .nonTerminalState(EblDocumentStatus.ISSUED)
          .successorNodes(EblDocumentStatus.SURRENDERED)
          .nonTerminalState(EblDocumentStatus.SURRENDERED)
          .successorNodes(EblDocumentStatus.VOID)
          .terminalStates(EblDocumentStatus.VOID)
          .unreachableStates(EblDocumentStatus.REJECTED)
          .build();

  private static final DFADefinition<String> AMENDMENT_EBL_DFA_DEFINITION =
      DFADefinition.builder(EblDocumentStatus.RECEIVED)
          .nonTerminalState(EblDocumentStatus.RECEIVED)
          .successorNodes(EblDocumentStatus.DRAFT, EblDocumentStatus.REJECTED)
          .nonTerminalState(EblDocumentStatus.DRAFT)
          .successorNodes(EblDocumentStatus.APPROVED)
          .nonTerminalState(EblDocumentStatus.APPROVED)
          .successorNodes(EblDocumentStatus.ISSUED)
          .nonTerminalState(EblDocumentStatus.ISSUED)
          .successorNodes(EblDocumentStatus.SURRENDERED)
          .nonTerminalState(EblDocumentStatus.SURRENDERED)
          .successorNodes(EblDocumentStatus.VOID)
          .terminalStates(EblDocumentStatus.REJECTED, EblDocumentStatus.VOID)
          .unreachableStates(EblDocumentStatus.PENDING_UPDATE, EblDocumentStatus.PENDING_APPROVAL)
          .build();

  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "shipping_instruction_reference")
  private String shippingInstructionReference;

  @Column(name = "document_status")
  @StringEnumValidation(value= EblDocumentStatus.class)
  private String documentStatus;

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
  @OneToMany(mappedBy = "shippingInstruction", cascade = CascadeType.ALL, orphanRemoval = true)
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
  private List<@Valid CustomsReference> customsReferences;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "shipping_instruction_id")
  private List<@Valid AdvanceManifestFilingEBL> advanceManifestFilings;

  @ElementCollection
  @Column(name = "requested_certificate", nullable = false)
  @CollectionTable(name = "requested_carrier_certificate", joinColumns = @JoinColumn(name = "shipping_instruction_id"))
  @OrderColumn(name = "element_order")
  private List<@Valid @PseudoEnum(value = "carriercertificates.csv", column = "Carrier Certificate Code",groups = AsyncShipperProvidedDataValidation.class)
    String> requestedCarrierCertificates;

  @ElementCollection
  @Column(name = "requested_clause", nullable = false)
  @CollectionTable(name = "requested_carrier_clause", joinColumns = @JoinColumn(name = "shipping_instruction_id"))
  @OrderColumn(name = "element_order")
  private List<@Valid @PseudoEnum(value = "carrierclauses.csv", column = "Carrier Clause Code",groups = AsyncShipperProvidedDataValidation.class)
    String> requestedCarrierClauses;

  // certain characteristics like the transport plan, are share among all shipments in the shipping
  // instruction, so it is beneficial to be able to retrieve one
  public Booking retrieveOneBooking() {
    return this.consignmentItems.stream()
        .map(ConsignmentItem::getBooking)
        .findAny()
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No confirmedBooking found in Shipping instruction with shipping instruction reference: "
                        + shippingInstructionReference));
  }

  @Transient private boolean isNew;

  public boolean isNew() {
    return id == null || isNew;
  }

  public ValidationResult<String> asyncValidation(Validator validator) {
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
      ? EblDocumentStatus.DRAFT
      : (amendmentToTransportDocument != null ? EblDocumentStatus.REJECTED : EblDocumentStatus.PENDING_UPDATE)
      ;
    return new ValidationResult<>(proposedStatus, validationErrors);
  }


  private void clearRequestedChanges() {
    if (this.requestedChanges != null && !this.requestedChanges.isEmpty()) {
      this.requestedChanges.clear();
    }
  }



  /** Transition the document into its {@link EblDocumentStatus#RECEIVED} state. */
  public void receive() {
    processTransition(EblDocumentStatus.RECEIVED, true);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#PENDING_UPDATE} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable in the Amendment
   * flow.
   */
  public void pendingUpdate() {
    processTransition(EblDocumentStatus.PENDING_UPDATE, false);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENDING_UPDATE} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when the EBL flow does
   * not support this state at all. I.e., calling {@link #pendingUpdate()} will trigger an
   * exception causing an internal server error status.
   */
  public boolean isPendingUpdateSupported() {
    return supportsState(EblDocumentStatus.PENDING_UPDATE);
  }

  /**
   * Transition the document into its {@link EblDocumentStatus#REJECTED} state.
   *
   * <p>This state is only reachable in the Amendment flow.
   */
  public void rejected(String reason) {
    processTransition(EblDocumentStatus.REJECTED, false);
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

  protected DFADefinition<String> getDfaDefinition() {
    return this.amendmentToTransportDocument != null
        ? AMENDMENT_EBL_DFA_DEFINITION
        : DEFAULT_EBL_DFA_DEFINITION;
  }

  @Override
  protected String getResumeFromState() {
    return this.documentStatus;
  }

  protected String getCurrentState() {
    var d = this.getDfa();
    if (d == null) {
      return this.getResumeFromState();
    }
    return d.getCurrentState();
  }

  @Override
  protected void transitionTo(String state) {
    super.transitionTo(state);
    this.documentStatus = state;
    this.shippingInstructionUpdatedDateTime = OffsetDateTime.now();
  }

  // Re-defined to make it visible to TransportDocument (protected also works like "package-private",
  // so we can keep it "non-public").
  @Override
  protected boolean supportsState(String state) {
    return super.supportsState(state);
  }

  protected void processTransition(
    String status,
    boolean clearRequestedChanges
  ) {
    processTransition(status, OffsetDateTime.now(), clearRequestedChanges);
  }

  protected void processTransition(
      String status,
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

  @Override
  protected RuntimeException errorForAttemptToLeaveTerminalState(
      String currentState,
      String successorState,
      CannotLeaveTerminalStateException e) {
    // Special-case for terminal states, where we can generate quite nice sounding
    // messages such as "... because the document is void (EblDocumentStatus.VOID)".
    return ConcreteRequestErrorMessageException.conflict(
        "Cannot perform the requested action on the document as the documentStatus is "
            + currentState,
        e);
  }

  @Override
  protected RuntimeException errorForTargetStateNotListedAsSuccessor(
      String currentState,
      String successorState,
      TargetStateIsNotSuccessorException e) {
    return ConcreteRequestErrorMessageException.conflict(
        "It is not possible to perform the requested action on the booking with documentStatus "
            + currentState,
        e);
  }

  public void assignConsignmentItems(@NotNull List<ConsignmentItem> consignmentItems) {
    this.consignmentItems = consignmentItems;
    for (var ci : consignmentItems) {
      ci.setShippingInstruction(this);
    }
  }
}
