package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "consignment_item")
public class ConsignmentItem {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "description_of_goods")
  private String descriptionOfGoods;

  @Column(name = "hs_code")
  private String hsCode;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipping_instruction_id")
  private ShippingInstruction shippingInstruction;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "shipment_id")
  private Shipment shipment;

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

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "consignmentItem")
  private Set<Reference> references;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "consignment_item_id", referencedColumnName = "id", nullable = false)
  // Since the cargoItem.id is generated it can happen that two cargoItems have the same values and
  // therefore cannot be added to the set
  private List<CargoItem> cargoItems;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    ConsignmentItem that = (ConsignmentItem) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
