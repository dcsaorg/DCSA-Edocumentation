package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.validation.Valid;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "utilized_transport_equipment")
public class UtilizedTransportEquipment {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "is_shipper_owned", nullable = false)
  private Boolean isShipperOwned;

  @Column(name = "cargo_gross_weight_unit")
  @Enumerated(EnumType.STRING)
  private WeightUnit cargoGrossWeightUnit;

  @Column(name = "cargo_gross_weight")
  private Double cargoGrossWeight;

  @Column(name = "equipment_reference")
  private String equipmentReference;

  @OneToOne
  @JoinColumn(name = "equipment")
  private Equipment equipment;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "utilized_transport_equipment_id", nullable = false)
  private Set<@Valid Seal> seals;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "utilized_transport_equipment_id")
  private List<CustomsReference> customsReferences;

}
