package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.dcsa.edocumentation.domain.persistence.entity.enums.AirExchangeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TemperatureUnit;

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
  @Column(name = "is_ventilation_open", nullable = false)
  private Boolean isVentilationOpen = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_drainholes_open", nullable = false)
  private Boolean isDrainholesOpen = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_controlled_atmosphere_required", nullable = false)
  private Boolean isControlledAtmosphereRequired = Boolean.FALSE;

  @Builder.Default
  @Column(name = "is_bulb_mode", nullable = false)
  private Boolean isBulbMode = Boolean.FALSE;

  @Column(name = "temperature_setpoint", nullable = false)
  private Float temperatureSetpoint;

  @Column(name = "temperature_unit", nullable = false)
  @Enumerated(EnumType.STRING)
  private TemperatureUnit temperatureUnit;

  @Column(name = "o2_setpoint")
  private Float o2Setpoint;

  @Column(name = "co2_setpoint")
  private Float co2Setpoint;

  @Column(name = "humidity_setpoint")
  private Float humiditySetpoint;

  @Column(name = "air_exchange_setpoint")
  private Float airExchangeSetpoint;

  @Column(name = "air_exchange_unit")
  @Enumerated(EnumType.STRING)
  private AirExchangeUnit airExchangeUnit;
}
