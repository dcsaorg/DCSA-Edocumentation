package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "active_reefer_settings")
public class ActiveReeferSettings {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "reefer_type_code", nullable = false)
  private ReeferType reeferType;

  @Column(name = "is_cargo_probe_1_required", nullable = false)
  private Boolean isCargoProbe1Required;

  @Column(name = "is_cargo_probe_2_required", nullable = false)
  private Boolean isCargoProbe2Required;

  @Column(name = "is_cargo_probe_3_required", nullable = false)
  private Boolean isCargoProbe3Required;

  @Column(name = "is_cargo_probe_4_required", nullable = false)
  private Boolean isCargoProbe4Required;

  @Column(name = "is_ventilation_open", nullable = false)
  private Boolean isVentilationOpen;

  @Column(name = "is_drainholes_open", nullable = false)
  private Boolean isDrainholesOpen;

  @Column(name = "is_bulb_mode", nullable = false)
  private Boolean isBulbMode;

  @Column(name = "is_gen_set_required", nullable = false)
  private Boolean isGenSetRequired;

  @Column(name = "is_pre_cooling_required", nullable = false)
  private Boolean isPreCoolingRequired;

  @Column(name = "is_cold_treatment_required", nullable = false)
  private Boolean isColdTreatmentRequired;

  @Column(name = "is_hot_stuffing_allowed", nullable = false)
  private Boolean isHotStuffingAllowed;

  @Column(name = "is_tracing_required", nullable = false)
  private Boolean isTracingRequired;

  @Column(name = "is_monitoring_required", nullable = false)
  private Boolean isMonitoringRequired;

  @Column(name = "is_high_value_cargo", nullable = false)
  private Boolean isHighValueCargo;

  @Column(name = "product_name", length = 500)
  private String productName;

  @Column(name = "extra_material", length = 500)
  private String extraMaterial;
}
