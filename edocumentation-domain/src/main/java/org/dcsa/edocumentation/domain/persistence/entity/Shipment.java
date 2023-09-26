package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.*;
import lombok.*;

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
          attributeNodes = {
            @NamedAttributeNode("loadLocation"),
            @NamedAttributeNode("dischargeLocation"),
          }),
    })
@Data
@Builder(toBuilder = true)
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
  private Set<@Valid ConsignmentItem> consignmentItems;

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
  @OneToMany(mappedBy = "shipment")
  private Set<ShipmentTransport> shipmentTransports = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipment")
  private Set<@Valid ShipmentLocation> shipmentLocations = new LinkedHashSet<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL)
  @OrderColumn(name = "list_order")
  private List<@Valid ShipmentCutOffTime> shipmentCutOffTimes;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @OrderColumn(name = "list_order")
  private List<ConfirmedEquipment> confirmedEquipments = new ArrayList<>();

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany
  @JoinTable(
      name = "shipment_carrier_clauses",
      joinColumns = {@JoinColumn(name = "shipment_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "carrier_clause_id", referencedColumnName = "id")})
  private Set<CarrierClause> carrierClauses = new LinkedHashSet<>();


  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipmentID")
  private Set<Charge> charges = new LinkedHashSet<>();

  public void assignConfirmedEquipments(List<ConfirmedEquipment> confirmedEquipments) {
    this.confirmedEquipments = Objects.requireNonNullElseGet(this.confirmedEquipments, ArrayList::new);
    this.confirmedEquipments.clear();
    this.confirmedEquipments.addAll(confirmedEquipments);
    for (var e : confirmedEquipments) {
      e.setShipment(this);  // For cascade to work properly
    }
  }

  public void assignShipmentCutOffTimes(List<ShipmentCutOffTime> shipmentCutOffTimes) {
    this.shipmentCutOffTimes = Objects.requireNonNullElseGet(this.shipmentCutOffTimes, ArrayList::new);
    this.shipmentCutOffTimes.clear();
    this.shipmentCutOffTimes.addAll(shipmentCutOffTimes);
    for (var t : shipmentCutOffTimes) {
      t.setShipment(this);  // For cascade to work properly
    }
  }

}
