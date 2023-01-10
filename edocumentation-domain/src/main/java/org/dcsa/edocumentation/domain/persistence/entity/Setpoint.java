package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.AirExchangeUnit;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TemperatureUnit;

import jakarta.persistence.*;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "setpoint")
public class Setpoint {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "temperature")
  private Float temperature;

  @Column(name = "temperature_unit")
  @Enumerated(EnumType.STRING)
  private TemperatureUnit temperatureUnit;

  @Column(name = "humidity")
  private Float humidity;

  @Column(name = "air_exchange")
  private Float airExchange;

  @Column(name = "air_exchange_unit")
  @Enumerated(EnumType.STRING)
  private AirExchangeUnit airExchangeUnit;

  @Column(name = "o2")
  private Float o2;

  @Column(name = "co2")
  private Float co2;

  @Column(name = "days_prior_to_discharge")
  private Float daysPriorToDischarge;
}
