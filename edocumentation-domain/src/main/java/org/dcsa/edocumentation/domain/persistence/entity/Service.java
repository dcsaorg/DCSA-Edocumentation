package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "service")
public class Service {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "carrier_id")
  private Carrier carrier;

  @Column(name = "carrier_service_code", length = 11)
  private String carrierServiceCode;

  @Column(name = "carrier_service_name", length = 50)
  private String carrierServiceName;

  @Column(name = "tradelane_id", length = 8)
  private String tradelaneId;

  @Column(name = "universal_service_reference", length = 8)
  private String universalServiceReference;
}
