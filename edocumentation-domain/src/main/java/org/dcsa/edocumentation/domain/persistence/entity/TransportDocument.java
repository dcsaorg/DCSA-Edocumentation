package org.dcsa.edocumentation.domain.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.LocationSubType;
import org.dcsa.edocumentation.domain.validations.LocationValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus.*;

@NamedEntityGraph(
  name = "graph.transportDocumentSummary",

  attributeNodes = {
    @NamedAttributeNode(
      value = "shippingInstruction",
      // Defined in ShippingInstruction, pulls a bit more data than what we need,
      // but saves us from maintaining the sub-graphs here.
      subgraph = "graph.transportDocumentSummary.shippingInstruction"
    ),
    @NamedAttributeNode("carrier"),
    @NamedAttributeNode("issuingParty")
  },

  subgraphs = {
    @NamedSubgraph(
      name = "graph.transportDocumentSummary.shippingInstruction",
      attributeNodes = {
        @NamedAttributeNode(
          value = "consignmentItems",
          subgraph = "graph.transportDocumentSummary.consignmentItem"
        )
      }),
    @NamedSubgraph(
      name = "graph.transportDocumentSummary.consignmentItem",
      attributeNodes = {
        @NamedAttributeNode("shipment")
      }),
    @NamedSubgraph(
      name = "graph.transportDocumentSummary.issuingParty",
      attributeNodes = {
        @NamedAttributeNode("address")
      }),

  }
)
@Getter
@Setter(AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "transport_document")
public class TransportDocument implements Persistable<UUID> {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "transport_document_reference")
  @Size(max = 20)
  private String transportDocumentReference;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "created_date_time")
  @CreatedDate
  protected OffsetDateTime transportDocumentCreatedDateTime;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "updated_date_time")
  @LastModifiedDate
  protected OffsetDateTime transportDocumentUpdatedDateTime;

  @Column(name = "issue_date")
  private LocalDate issueDate;

  @Column(name = "shipped_onboard_date")
  private LocalDate shippedOnBoardDate;

  @Column(name = "received_for_shipment_date")
  private LocalDate receivedForShipmentDate;

  @Column(name = "declared_value_currency_code", length = 3)
  private String declaredValueCurrency;

  @Column(name = "declared_value")
  private Float declaredValue;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_instruction_id", nullable = false)
  private ShippingInstruction shippingInstruction;

  @Column(name = "number_of_rider_pages")
  private Integer numberOfRiderPages;

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "place_of_issue_id")
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location placeOfIssue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "issuing_party_id")
  private Party issuingParty;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "transport_document_id", referencedColumnName = "id")
  private Set<Charge> charges;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany
  @JoinTable(
    name = "shipment_carrier_clauses",
    joinColumns = {@JoinColumn(name = "transport_document_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "carrier_clause_id", referencedColumnName = "id")})
  private Set<CarrierClause> carrierClauses = new LinkedHashSet<>();

  @Transient private boolean isNew;

  public boolean isNew() {
    return id == null || isNew;
  }


  /** Transition the document into its {@link EblDocumentStatus#DRFT} state. */
  public void draft() {
    processTransition(DRFT);
  }


  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable in the Amendment
   * flow.
   */
  public void pendingApproval() {
    processTransition(PENA);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when the EBL flow does
   * not support this state at all. I.e., calling {@link #pendingApproval()} will trigger an
   * exception causing an internal server error status.
   */
  public boolean isPendingApprovalSupported() {
    return shippingInstruction.supportsState(PENA);
  }

  /** Transition the document into its {@link EblDocumentStatus#APPR} state. */
  public void approveFromShipper() {
    if (this.shippingInstruction.getCurrentState() != DRFT) {
      throw ConcreteRequestErrorMessageException.conflict("Cannot approveFromShipper: The TransportDocument is not in the DRFT state", null);
    }
    processTransition(APPR);
  }


  /** Transition the document into its {@link EblDocumentStatus#APPR} state. */
  public void approveFromCarrier() {
    if (this.shippingInstruction.getCurrentState() != PENA) {
      throw ConcreteRequestErrorMessageException.conflict("Cannot approveFromCarrier: The TransportDocument is not in the PENA state", null);
    }
    processTransition(APPR);
  }

  /** Transition the document into its {@link EblDocumentStatus#ISSU} state. */
  public void issue() {
    issue(null, null);
  }

  /** Transition the document into its {@link EblDocumentStatus#ISSU} state.
   *
   * @param issueDate Use the provided date as issue date. Should be LocalDate.now() or in the past. If null,
   *                  defaults to LocalDate.now()
   * @param shipmentDate Use the provided date as the shippedOnBoardDate/receivedForShipmentDate depending on
   *                     the type of B/L being issued. If null, keep the original value (if present) or
   *                     use LocalDate.now() (if the original value was absent).
   */
  public void issue(LocalDate issueDate, LocalDate shipmentDate) {
    processTransition(ISSU);

    if (shippingInstruction.getIsShippedOnBoardType() == Boolean.TRUE) {
      if (shipmentDate != null || this.shippedOnBoardDate == null) {
        this.shippedOnBoardDate = Objects.requireNonNullElseGet(shipmentDate, LocalDate::now);
      }
    } else {
      if (shipmentDate != null || this.receivedForShipmentDate == null) {
        this.receivedForShipmentDate = Objects.requireNonNullElseGet(shipmentDate, LocalDate::now);
      }
    }

    this.issueDate = Objects.requireNonNullElseGet(issueDate, LocalDate::now);
  }

  /** Transition the document into its {@link EblDocumentStatus#SURR} state. */
  public void surrender() {
    processTransition(SURR);
  }

  /** Transition the document into its {@link EblDocumentStatus#VOID} state. */
  // "void" is a keyword and cannot be used as a method name.
  public void voidDocument() {
    processTransition(VOID);
  }

  protected void processTransition(EblDocumentStatus status) {
    shippingInstruction.processTransition(status, true);
    if (id == null) {
      id = UUID.randomUUID();
      isNew = true;
    }
    if (transportDocumentReference == null) {
      transportDocumentReference = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
    OffsetDateTime updateTime = OffsetDateTime.now();
    this.transportDocumentUpdatedDateTime = updateTime;
    if (this.transportDocumentCreatedDateTime == null) {
      this.transportDocumentCreatedDateTime = updateTime;
    }
  }

}
