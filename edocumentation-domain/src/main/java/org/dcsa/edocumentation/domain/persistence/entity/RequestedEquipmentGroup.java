package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;
import org.dcsa.skernel.infrastructure.validation.ISO6346EquipmentReference;

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
  @JoinColumn(name = "booking_request_id")
  @Setter(AccessLevel.PACKAGE)
  private BookingRequest bookingRequest;

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
  private List<@Valid @NotBlank @ISO6346EquipmentReference(groups = AsyncShipperProvidedDataValidation.class) String> equipmentReferences;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "active_reefer_settings_id")
  private ActiveReeferSettings activeReeferSettings;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OrderColumn(name = "list_order")
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "requestedEquipment")
  private List<@Valid Commodity> commodities;

  public void assignCommodities(@NonNull List<Commodity> commodities) {
    this.commodities = commodities;
    for (var c : commodities) {
      c.setRequestedEquipment(this);
    }
  }
}
