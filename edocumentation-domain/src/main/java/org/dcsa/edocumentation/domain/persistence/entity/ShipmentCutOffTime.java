package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_cutoff_time")
public class ShipmentCutOffTime {

  @Id
  @GeneratedValue
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id")
  // Used by Shipment to make the JPA relations work
  @Setter(AccessLevel.PACKAGE)
  private Shipment shipment;

  @Column(name = "cut_off_time_code")
  @PseudoEnum("cutofftimecodes.csv")
  private String cutOffDateTimeCode;

  @Column(name = "cut_off_time", length = 35)
  private OffsetDateTime cutOffDateTime;

}
