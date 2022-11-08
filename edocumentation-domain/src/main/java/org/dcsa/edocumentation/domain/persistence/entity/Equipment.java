package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "equipment")
public class Equipment {

  @Id
  @Column(name = "equipment_reference")
  @Size(max = 15)
  private String equipmentReference;

  @Column(name = "iso_equipment_code", columnDefinition = "bpchar")
  @Size(max = 4)
  private String isoEquipmentCode;

  @Column(name = "tare_weight")
  private Float tareWeight;

  @Column(name = "weight_unit")
  @Size(max = 3)
  private String weightUnit;
}
