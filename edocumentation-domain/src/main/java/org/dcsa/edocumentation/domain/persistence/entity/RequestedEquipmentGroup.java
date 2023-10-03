package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "requested_equipment_group")
public class RequestedEquipmentGroup {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  @Setter(AccessLevel.PACKAGE)
  private Booking booking;

  @Column(name = "iso_equipment_code", nullable = false)
  @Size(max = 4)
  @PseudoEnum(value = "isoequipmentcodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String isoEquipmentCode;

  @Column(name = "units", nullable = false)
  private Integer units;

  @Column(name = "tare_weight")
  private Float tareWeight;

  @Column(name = "tare_weight_unit")
  @Enumerated(EnumType.STRING)
  private WeightUnit tareWeightUnit;

  @Column(name = "is_shipper_owned")
  private Boolean isShipperOwned;

  @ElementCollection
  @Column(name = "equipment_reference", nullable = false)
  @CollectionTable(name = "requested_equipment_group_equipment_references", joinColumns = @JoinColumn(name = "requested_equipment_group_id"))
  @OrderColumn(name = "list_order")
  private List<String> equipmentReferences;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "active_reefer_settings_id")
  private ActiveReeferSettings activeReeferSettings;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "requestedEquipment")
  private List<Commodity> commodities;

  public void assignCommodities(@NonNull List<Commodity> commodities) {
    this.commodities = commodities;
    for (var c : commodities) {
      c.setRequestedEquipment(this);
    }
  }
}
