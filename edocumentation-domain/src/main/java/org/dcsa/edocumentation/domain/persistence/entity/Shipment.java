package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.skernel.domain.persistence.entity.Carrier;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraph(
    name = "graph.shipment-summary",
    attributeNodes = {@NamedAttributeNode("booking")})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment")
public class Shipment {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipment")
  private Set<ConsignmentItem> consignmentItems;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;

  @Column(name = "carrier_booking_reference", length = 35)
  private String carrierBookingReference;

  @Column(name = "terms_and_conditions", length = 35)
  private String termsAndConditions;

  @Column(name = "confirmation_datetime", length = 35)
  private OffsetDateTime shipmentCreatedDateTime;

  @Column(name = "updated_date_time", length = 35)
  private OffsetDateTime shipmentUpdatedDateTime;
}
