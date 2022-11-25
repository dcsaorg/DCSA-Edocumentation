package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.skernel.domain.persistence.entity.Carrier;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@NamedEntityGraph(
    name = "graph.shipment-summary",
    attributeNodes = {@NamedAttributeNode("booking")})
@NamedEntityGraph(
    name = "graph.shipment",
    attributeNodes = {
      @NamedAttributeNode("booking"),
      @NamedAttributeNode("carrier"),
      @NamedAttributeNode(value = "shipmentLocations", subgraph = "subgraph.shipmentLocations"),
      @NamedAttributeNode(value = "shipmentTransports", subgraph = "subgraph.shipmentTransports"),
      @NamedAttributeNode("shipmentCutOffTimes"),
      @NamedAttributeNode("carrierClauses"),
      @NamedAttributeNode("confirmedEquipments"),
      @NamedAttributeNode("charges")
    },
    subgraphs = {
      @NamedSubgraph(
          name = "subgraph.shipmentLocations",
          attributeNodes = {@NamedAttributeNode("location")}),
      @NamedSubgraph(
          name = "subgraph.shipmentTransports",
          attributeNodes = {@NamedAttributeNode("transport")}),
    })
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

  @Column(name = "confirmation_datetime")
  private OffsetDateTime shipmentCreatedDateTime;

  @Column(name = "updated_date_time")
  private OffsetDateTime shipmentUpdatedDateTime;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipmentID")
  private Set<ShipmentTransport> shipmentTransports = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipmentID")
  private Set<ShipmentLocation> shipmentLocations = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipmentID")
  private Set<ShipmentCutOffTime> shipmentCutOffTimes = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id")
  private Set<RequestedEquipmentGroup> confirmedEquipments = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany
  @JoinTable(
      name = "shipment_carrier_clauses",
      joinColumns = {@JoinColumn(name = "shipment_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "carrier_clause_id", referencedColumnName = "id")})
  private Set<CarrierClause> carrierClauses = new LinkedHashSet<>();

  @OneToMany(mappedBy = "shipmentID")
  private Set<Charge> charges = new LinkedHashSet<>();
}
