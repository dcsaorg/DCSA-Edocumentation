package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.CargoItemValidation;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "cargo_item")
@CargoItemValidation(groups = AsyncShipperProvidedDataValidation.class)
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

  @Column(name = "equipment_reference", nullable = false)
  private String equipmentReference;

  @ElementCollection
  @Column(name = "shipping_mark", nullable = false)
  @CollectionTable(name = "shipping_mark", joinColumns = @JoinColumn(name = "cargo_item"))
  @OrderColumn(name = "element_order")
  private List<String> shippingMarks;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "utilized_transport_equipment_id")
  private @Valid UtilizedTransportEquipment utilizedTransportEquipment;

  @OrderColumn(name = "list_order")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "cargo_item_id")
  private List<@Valid CustomsReference> customsReferences;

  public void assignEquipment(UtilizedTransportEquipment utilizedTransportEquipment) {
    if (!this.equipmentReference.equals(utilizedTransportEquipment.getEquipmentReference())) {
      throw new IllegalArgumentException("The provided utilizedTransportEquipment had the wrong equipment reference");
    }
    this.utilizedTransportEquipment = utilizedTransportEquipment;
  }
}
