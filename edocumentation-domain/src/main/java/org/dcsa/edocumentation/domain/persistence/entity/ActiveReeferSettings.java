package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType;

import javax.persistence.*;
import java.util.Set;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "reefer_type_code", nullable = false)
  private ReeferType type;

  @Builder.Default
  @Column(name = "is_cargo_probe_1_required", nullable = false)
  private Boolean isCargoProbe1Required = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_cargo_probe_2_required", nullable = false)
  private Boolean isCargoProbe2Required = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_cargo_probe_3_required", nullable = false)
  private Boolean isCargoProbe3Required = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_cargo_probe_4_required", nullable = false)
  private Boolean isCargoProbe4Required = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_ventilation_open", nullable = false)
  private Boolean isVentilationOpen = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_drainholes_open", nullable = false)
  private Boolean isDrainholesOpen = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_bulb_mode", nullable = false)
  private Boolean isBulbMode = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_gen_set_required", nullable = false)
  private Boolean isGeneratorSetRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_pre_cooling_required", nullable = false)
  private Boolean isPreCoolingRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_cold_treatment_required", nullable = false)
  private Boolean isColdTreatmentRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_hot_stuffing_allowed", nullable = false)
  private Boolean isHotStuffingAllowed = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_tracing_required", nullable = false)
  private Boolean isTracingRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_monitoring_required", nullable = false)
  private Boolean isMonitoringRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_high_value_cargo", nullable = false)
  private Boolean isHighValueCargo = Boolean.FALSE;

  @Column(name = "product_name", length = 500)
  private String productName;

  @Column(name = "extra_material", length = 500)
  private String extraMaterial;

  // @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "active_reefer_settings_id", nullable = false, referencedColumnName = "id")
  Set<Setpoint> setpoints;
}
