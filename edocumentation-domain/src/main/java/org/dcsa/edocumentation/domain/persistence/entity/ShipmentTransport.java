package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportPlanStageCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_transport")
public class ShipmentTransport {

  @Id private UUID id;

  @Column(name = "shipment_id")
  private UUID shipmentID;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "transport_id")
  private Transport transport;

  @Column(name = "transport_plan_stage_sequence_number")
  private Integer transportPlanStageSequenceNumber;

  @Column(name = "transport_plan_stage_code")
  @Enumerated(EnumType.STRING)
  private TransportPlanStageCode transportPlanStageCode;

  @Column(name = "commercial_voyage_id")
  private UUID commercialVoyageID;

  @Column(name = "is_under_shippers_responsibility")
  private Boolean isUnderShippersResponsibility;
}
