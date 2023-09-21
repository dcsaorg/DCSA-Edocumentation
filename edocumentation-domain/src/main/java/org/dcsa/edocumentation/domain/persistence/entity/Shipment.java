package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.*;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.AssignedEquipment;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.EquipmentAssignment;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;

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
          })
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


  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "shipmentID")
  private Set<Charge> charges = new LinkedHashSet<>();


  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id", referencedColumnName = "id", nullable = false)
  private Set<AssignedEquipment> assignedEquipments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id")
  private Set<AdvanceManifestFiling> advanceManifestFilings = new LinkedHashSet<>();

  public void assignEquipments(List<EquipmentAssignment> equipmentAssignments) {
    var requestedEquipmentGroupTable = requestedEquipmentGroupTable(booking.getRequestedEquipments());
    List<AssignedEquipment> assignments = new ArrayList<>();
    if (equipmentAssignments.isEmpty()) {
      if (booking.getRequestedEquipments().isEmpty()) {
        this.assignedEquipments = Set.of();
      }
      else{
      throw ConcreteRequestErrorMessageException.invalidInput("equipmentAssignments must be non-empty when the booking requires equipments");
      }
    }
    for (var equipmentAssignment : equipmentAssignments) {
      var unfilledRequestedEquipmentGroup = requestedEquipmentGroupTable.get(equipmentAssignment.getMatchKey());
      var reeferMsg = " without any reefer";
      if (equipmentAssignment.requestedReeferType() != null) {
        reeferMsg = " with reefer type " + equipmentAssignment.requestedReeferType();
      }
      if (unfilledRequestedEquipmentGroup == null) {
        throw ConcreteRequestErrorMessageException.invalidInput("The booking " + booking.getCarrierBookingRequestReference()
          + " did not have any requests for equipment of " + equipmentAssignment.requestedISOEquipmentCode()
          + reeferMsg + ". (Note: RequestedEquipmentGroups with isShipperOwned: true is are ignored)");
      }
      int totalUnfilled = unfilledRequestedEquipmentGroup.getUnfilledCount();
      if (totalUnfilled != equipmentAssignment.equipments().size()) {
        throw ConcreteRequestErrorMessageException.invalidInput("The booking " + booking.getCarrierBookingRequestReference()
          + " requested a total of " + totalUnfilled + " equipments (rounded up) with of " + equipmentAssignment.requestedISOEquipmentCode()
          + reeferMsg + ", but the request provided " + equipmentAssignment.equipments().size()
          + ".  The these numbers should match exactly. (Note: RequestedEquipmentGroups with isShipperOwned: true are ignored)");
      }
      performAssign(assignments, equipmentAssignment.equipments(), unfilledRequestedEquipmentGroup.requestedEquipmentGroups);
    }
    this.assignedEquipments = Set.copyOf(assignments);
  }

  private void performAssign(List<AssignedEquipment> assignments, List<Equipment> equipments, List<RequestedEquipmentGroup> groups) {
    int nextEquipmentIndex = equipments.size();
    for (RequestedEquipmentGroup requestedEquipmentGroup : groups) {
      int missingUnits = (int) Math.ceil(requestedEquipmentGroup.getRequestedEquipmentUnits());
      assert missingUnits < nextEquipmentIndex;
      int endIndex = nextEquipmentIndex;
      int startIndex = endIndex - missingUnits;
      nextEquipmentIndex -= startIndex;

      assignments.add(
        AssignedEquipment.builder()
          .equipmentReferences(Set.copyOf(equipments.subList(startIndex, endIndex)))
            .requestedEquipmentGroup(requestedEquipmentGroup)
            .build()
          );
      requestedEquipmentGroup.equipmentProvidedForShipment(this);
    }
  }

  private Map<String, UnfilledRequestedEquipmentGroup> requestedEquipmentGroupTable(Collection<RequestedEquipmentGroup> requestedEquipmentGroups) {
    /* Simplifications/Assumptions in this code that you are welcome to fix at your own peril!
     *
     *   1. This code assumes that all reefer containers with a given ISO code and reefer type
     *      support exactly the same ActiveReeferSettings (e.g., same amount of probes).
     *   2. The provided containers are size-compatible with the request regardless of their
     *      actual ISO code.
     *   3. The code assumes that container groups with "isShipperOwned": true are all satisfied
     *      already.
     */
    HashMap<String, UnfilledRequestedEquipmentGroup> table = new HashMap<>();
    for (RequestedEquipmentGroup requestedEquipmentGroup : requestedEquipmentGroups) {
      if (requestedEquipmentGroup.getIsShipperOwned() == Boolean.TRUE || requestedEquipmentGroup.getShipment() != null) {
        // If the request is for shipper owner containers *OR* already part of a shipment,
        // we assume it can be handled.
        continue;
      }
      UnfilledRequestedEquipmentGroup unfilledGroup = new UnfilledRequestedEquipmentGroup();
      unfilledGroup.add(requestedEquipmentGroup);
    }
    return table;
  }

  private static class UnfilledRequestedEquipmentGroup {
    final List<RequestedEquipmentGroup> requestedEquipmentGroups = new ArrayList<>();
    double unfilledCount = 0;

    public void add(RequestedEquipmentGroup requestedEquipmentGroup) {
      this.requestedEquipmentGroups.add(requestedEquipmentGroup);
      this.unfilledCount += requestedEquipmentGroup.getRequestedEquipmentUnits();
    }

    public int getUnfilledCount() {
      return (int)Math.ceil(unfilledCount);
    }
  }

}
