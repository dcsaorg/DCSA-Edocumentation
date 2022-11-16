package org.dcsa.edocumentation.domain.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.dcsa.skernel.domain.persistence.entity.Carrier;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transport_document")
public class TransportDocument {

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

  @Column(name = "number_of_originals")
  private Integer numberOfOriginals;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipping_instruction_id")
  private ShippingInstruction shippingInstruction;

  @Column(name = "number_of_rider_pages")
  private Integer numberOfRiderPages;

  @Column(name = "valid_until")
  private OffsetDateTime validUntil;

  @Column(name = "place_of_issue_id")
  private UUID placeOfIssue;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "issuing_party_id")
  private Party issuingParty;
}
