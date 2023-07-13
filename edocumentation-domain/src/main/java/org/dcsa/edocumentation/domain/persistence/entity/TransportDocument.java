package org.dcsa.edocumentation.domain.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.springframework.data.domain.Persistable;
import org.dcsa.skernel.domain.persistence.entity.Location;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
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
  protected OffsetDateTime transportDocumentCreatedDateTime;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @Column(name = "updated_date_time")
  protected OffsetDateTime transportDocumentUpdatedDateTime;

  @Column(name = "issue_date")
  private LocalDate issueDate;

  @Column(name = "shipped_onboard_date")
  private LocalDate shippedOnBoardDate;

  @Column(name = "received_for_shipment_date")
  private LocalDate receivedForShipmentDate;

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
  public ShipmentEvent draft() {
    return processTransition(DRFT, null);
  }


  /**
   * Transition the document into its {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. E.g., it is not reachable in the Amendment
   * flow.
   */
  public ShipmentEvent pendingApproval(String reason) {
    // TODO: Should this be moved to the TRD?
    return processTransition(PENA, reason);
  }

  /**
   * Check whether the flow supports the {@link EblDocumentStatus#PENA} state.
   *
   * <p>This state is not supported in all EBL flows. This will return false when the EBL flow does
   * not support this state at all. I.e., calling {@link #pendingApproval(String)} will trigger an
   * exception causing an internal server error status.
   */
  public boolean isPendingApprovalSupported() {
    return shippingInstruction.supportsState(PENA);
  }

  /** Transition the document into its {@link EblDocumentStatus#APPR} state. */
  public ShipmentEvent approve() {
    return processTransition(APPR, null);
  }

  /** Transition the document into its {@link EblDocumentStatus#ISSU} state. */
  public ShipmentEvent issue() {
    ShipmentEvent event = processTransition(ISSU, null);
    if (issueDate == null) {
      // Ensure we have an issue date.
      this.issueDate = LocalDate.now();
    }
    return event;
  }

  /** Transition the document into its {@link EblDocumentStatus#SURR} state. */
  public ShipmentEvent surrender() {
    return processTransition(SURR, null);
  }

  /** Transition the document into its {@link EblDocumentStatus#VOID} state. */
  // "void" is a keyword and cannot be used as a method name.
  public ShipmentEvent voidDocument() {
    return processTransition(VOID, null);
  }

  protected ShipmentEvent processTransition(EblDocumentStatus status, String reason) {
    return shippingInstruction.processTransition(status, reason, this::shipmentEventTRDBuilder);
  }

  protected ShipmentEvent.ShipmentEventBuilder<?, ?> shipmentEventTRDBuilder(OffsetDateTime updateTime) {
    this.transportDocumentUpdatedDateTime = updateTime;
    if (this.transportDocumentCreatedDateTime == null) {
      this.transportDocumentCreatedDateTime = updateTime;
    }
    if (id == null) {
      id = UUID.randomUUID();
      isNew = true;
    }
    if (transportDocumentReference == null) {
      transportDocumentReference = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
    }
    return ShipmentEvent.builder()
      .documentID(id)
      .documentReference(transportDocumentReference)
      .documentTypeCode(DocumentTypeCode.TRD);
  }

}
