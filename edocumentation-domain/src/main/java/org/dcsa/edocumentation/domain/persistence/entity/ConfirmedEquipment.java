package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "confirmed_equipment")
public class ConfirmedEquipment {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "shipment_id")
  // Used by Shipment to make the JPA relations work
  @Setter(AccessLevel.PACKAGE)
  private Shipment shipment;

  @Column(name = "iso_equipment_code", nullable = false)
  @Size(max = 4)
  @PseudoEnum(value = "isoequipmentcodes.csv")
  private String isoEquipmentCode;

  @Column(name = "units", nullable = false)
  private Integer units;

}
