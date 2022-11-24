package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "requested_equipment")
public class RequestedEquipment {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 4)
  @Column(name = "requested_equipment_sizetype")
  private String sizeType;

  @Column(name = "requested_equipment_units")
  private Integer units;

  @Size(max = 4)
  @Column(name = "confirmed_equipment_sizetype")
  private String confirmedEquipmentSizetype;

  @Column(name = "confirmed_equipment_units")
  private Integer confirmedEquipmentUnits;

  @Column(name = "is_shipper_owned")
  private Boolean isShipperOwned = false;

  @Column(name = "shipment_id")
  private UUID shipmentID;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany
  @JoinTable(
      name = "requested_equipment_equipment",
      joinColumns = @JoinColumn(name = "requested_equipment_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "equipment_reference", referencedColumnName = "equipment_reference")
  )
  private Set<Equipment> equipments;

}
