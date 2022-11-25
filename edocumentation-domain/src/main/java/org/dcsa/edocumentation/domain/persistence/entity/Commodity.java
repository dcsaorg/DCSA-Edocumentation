package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "commodity")
public class Commodity {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @Column(name = "commodity_type", nullable = false, length = 550)
  private String commodityType;

  @Column(name = "hs_code", length = 10) // we need an enum for this
  private String hsCode;

  @Column(name = "cargo_gross_weight")
  private Float cargoGrossWeight;

  @Column(name = "cargo_gross_weight_unit")
  @Enumerated(EnumType.STRING)
  private WeightUnit cargoGrossWeightUnit;

  @Column(name = "cargo_gross_volume")
  private Float cargoGrossVolume;

  @Column(name = "cargo_gross_volume_unit")
  @Enumerated(EnumType.STRING)
  private VolumeUnit cargoGrossVolumeUnit;

  @Column(name = "number_of_packages")
  private Integer numberOfPackages;

  @Column(name = "export_license_issue_date")
  private LocalDate exportLicenseIssueDate;

  @Column(name = "export_license_expiry_date")
  private LocalDate exportLicenseExpiryDate;

  /**
   * Note commodity is linked to requested_equipment_group via join table requested_equipment_commodity.
   * This is currently implemented on RequestedEquipmentGroup on the assumptions that that is the direction we would
   * need it. Move/Copy to here if needed in the other direction.
   */
}
