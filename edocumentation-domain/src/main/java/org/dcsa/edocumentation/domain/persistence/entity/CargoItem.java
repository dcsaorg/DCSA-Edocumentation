package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "cargo_item")
public class CargoItem {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "number_of_packages")
  private Integer numberOfPackages;

  @Column(name = "weight")
  private Double weight;

  @Column(name = "weight_unit")
  @Enumerated(EnumType.STRING)
  private WeightUnit weightUnit;

  @Column(name = "volume")
  private Double volume;

  @Column(name = "volume_unit")
  @Enumerated(EnumType.STRING)
  private VolumeUnit volumeUnit;

  @Column(name = "package_code")
  private String packageCode;

  @Column(name = "package_name_on_bl")
  private String packageNameOnBl;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "cargo_item_id", nullable = false, referencedColumnName = "id")
  private Set<CargoLineItem> cargoLineItems;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "utilized_transport_equipment_id")
  private UtilizedTransportEquipment utilizedTransportEquipment;

}
