package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.Set;
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
  @JoinColumn(name = "shipment_id")
  private Shipment shipment;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @Column(name = "requested_equipment_iso_equipment_code", nullable = false)
  @Size(max = 4)
  private String requestedEquipmentIsoEquipmentCode;

  @Column(name = "requested_equipment_units", nullable = false)
  private Float requestedEquipmentUnits;

  @Column(name = "confirmed_equipment_iso_equipment_code")
  @Size(max = 4)
  private String confirmedEquipmentIsoEquipmentCode;

  @Column(name = "confirmed_equipment_units")
  private Integer confirmedEquipmentUnits;

  @Column(name = "is_shipper_owned")
  private Boolean isShipperOwned;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "active_reefer_settings_id")
  private ActiveReeferSettings activeReeferSettings;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "requested_equipment_group_id")
  private Set<UtilizedTransportEquipment> utilizedTransportEquipments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "commodity_id")
  private Commodity commodity;

  public void equipmentProvidedForShipment(Shipment shipment) {
    this.shipment = shipment;
  }

  public String getMatchKey() {
    String reeferKey = "<NONE>";
    if (activeReeferSettings != null) {
      reeferKey = activeReeferSettings.getType().name();
    }
    return requestedEquipmentIsoEquipmentCode + "/" + reeferKey;
  }
}
