package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.WeightUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

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

  @Enumerated(EnumType.STRING)
  @Column(name = "weight_unit")
  private WeightUnit weightUnit;
}
