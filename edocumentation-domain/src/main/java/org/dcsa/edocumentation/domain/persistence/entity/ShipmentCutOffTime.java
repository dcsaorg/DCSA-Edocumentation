package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CutOffDateTimeCode;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_cutoff_time")
@IdClass(ShipmentCutOffTimeId.class)
public class ShipmentCutOffTime {

  @Id
  @Column(name = "shipment_id")
  private UUID shipmentID;

  @Id
  @Column(name = "cut_off_time_code")
  @Enumerated(EnumType.STRING)
  private CutOffDateTimeCode cutOffDateTimeCode;

  @Column(name = "cut_off_time", length = 35)
  private OffsetDateTime cutOffDateTime;

}
