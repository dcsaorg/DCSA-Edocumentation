package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.domain.persistence.entity.Location;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraph(
  name = "graph.shipping-instruction-summary",
  attributeNodes = {
    @NamedAttributeNode("shipments")
  }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipping_instruction")
public class ShippingInstruction {

  @Id
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
  private Boolean isShippedOnboardType;

  @Column(name = "number_of_copies")
  private Integer numberOfCopies;

  @Column(name = "number_of_originals")
  private Integer numberOfOriginals;

  @Column(name = "is_electronic")
  private Boolean isElectronic;

  @Column(name = "is_to_order")
  private Boolean isToOrder;

  @Column(name = "are_charges_displayed_on_originals")
  private Boolean areChargesDisplayedOnOriginals;

  @Column(name = "are_charges_displayed_on_copies")
  private Boolean areChargesDisplayedOnCopies;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "place_of_issue_id")
  private Location placeOfIssueID;

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
  @OneToMany
  @JoinTable
    (
      name="consignment_item",
      joinColumns={ @JoinColumn(name="shipping_instruction_id", referencedColumnName="id") },
      inverseJoinColumns={ @JoinColumn(name="shipment_id", referencedColumnName="id", unique=true) }
    )
  private Set<Shipment> shipments = new LinkedHashSet<>();

}
