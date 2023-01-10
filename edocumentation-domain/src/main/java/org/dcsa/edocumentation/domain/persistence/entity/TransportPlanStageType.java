package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.TransportPlanStageCode;

import jakarta.persistence.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "transport_plan_stage_type")
public class TransportPlanStageType {

  @Id
  @Column(name = "transport_plan_stage_code", length = 3, nullable = false)
  @Enumerated(EnumType.STRING)
  private TransportPlanStageCode transportPlanStageCode;

  @Column(name = "transport_plan_stage_name", length = 100)
  private String transportPlanStageName;

  @Column(name = "transport_plan_stage_description", length = 250)
  private String transportPlanStageDescription;

}
