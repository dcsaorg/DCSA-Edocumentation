package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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

  @Column(name = "commodity_type", nullable = false, length = 550)
  private String commodityType;

  @Column(name = "commodity_subreference", length = 100)
  private String commoditySubreference;

  @ElementCollection
  @Column(name = "hs_code", nullable = false)
  @CollectionTable(name = "hs_code_item", joinColumns = @JoinColumn(name = "commodity_id"))
  @OrderColumn(name = "element_order")
  private List<String> hsCodes;

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

  @Column(name = "export_license_issue_date")
  private LocalDate exportLicenseIssueDate;

  @Column(name = "export_license_expiry_date")
  private LocalDate exportLicenseExpiryDate;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "requested_equipment_group_id")
  @Setter(AccessLevel.PACKAGE)
  private RequestedEquipmentGroup requestedEquipment;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @JoinColumn(name = "outer_packaging_id")
  private OuterPackaging outerPackaging;

  public void assignSubreference(String s) {
    if (this.commoditySubreference != null && !this.commoditySubreference.equals(s)) {
      // If the subreference can change, then it can invalidate any existing SI pointing to this
      // commodity. Detecting this and cleaning it this up is not really worth for the RI.
      throw new IllegalStateException("Cannot change the commoditySubreference once assigned " +
        "(this restriction is a RI implementation detail)"
      );
    }
    this.commoditySubreference = s;
  }
}
