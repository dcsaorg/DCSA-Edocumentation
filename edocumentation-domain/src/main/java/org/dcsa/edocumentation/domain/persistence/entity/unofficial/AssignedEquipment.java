package org.dcsa.edocumentation.domain.persistence.entity.unofficial;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "assigned_equipment")
public class AssignedEquipment {

  @Id
  @GeneratedValue
  private UUID id;

  @OneToMany
  @JoinTable(
    name = "assigned_equipment_references",
    joinColumns = @JoinColumn(
      name = "assigned_equipment_id",
      referencedColumnName = "id"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "equipment_reference",
      referencedColumnName = "equipment_reference"
    )
  )
  private Set<Equipment> equipmentReferences;

  @OneToOne(optional = false)
  private RequestedEquipmentGroup requestedEquipmentGroup;

}
