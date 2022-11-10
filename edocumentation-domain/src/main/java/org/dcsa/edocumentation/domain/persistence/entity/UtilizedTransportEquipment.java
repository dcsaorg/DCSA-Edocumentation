package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
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

  @OneToOne
  @JoinColumn(name = "equipment_reference")
  private Equipment equipment;

  //ToDo associations with ActiveReeferSettings and Seals

}
