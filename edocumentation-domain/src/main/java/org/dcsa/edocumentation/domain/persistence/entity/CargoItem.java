package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

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

  @ElementCollection
  @Column(name = "shipping_mark", nullable = false)
  @CollectionTable(name = "shipping_mark", joinColumns = @JoinColumn(name = "cargo_item"))
  @OrderColumn(name = "element_order")
  private List<String> shippingMarks;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "utilized_transport_equipment_id")
  private UtilizedTransportEquipment utilizedTransportEquipment;

}
