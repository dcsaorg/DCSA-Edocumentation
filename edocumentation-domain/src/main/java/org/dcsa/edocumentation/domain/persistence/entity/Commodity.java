package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VolumeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import javax.persistence.*;
import javax.validation.constraints.Size;
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
  UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @Column(name = "commodity_type")
  @Size(max = 550)
  private String commodityType;

  @Column(name = "hs_code") // we need an enum for this
  private String hsCode;

  @Column(name = "cargo_gross_weight")
  private Double cargoGrossWeight;

  @Column(name = "cargo_gross_weight_unit")
  @Enumerated(EnumType.STRING)
  private WeightUnit cargoGrossWeightUnit;

  @Column(name = "cargo_gross_volume")
  private Double cargoGrossVolume;

  @Column(name = "cargo_gross_volume_unit")
  @Enumerated(EnumType.STRING)
  private VolumeUnit cargoGrossVolumeUnit;

  @Column(name = "number_of_packages")
  private Integer numberOfPackages;

  @Column(name = "export_license_issue_date")
  private LocalDate exportLicenseIssueDate;

  @Column(name = "export_license_expiry_date")
  private LocalDate exportLicenseExpiryDate;
}
