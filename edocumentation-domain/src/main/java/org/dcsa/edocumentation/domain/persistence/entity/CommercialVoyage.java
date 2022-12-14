package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "commercial_voyage")
public class CommercialVoyage {

  @Id
  @GeneratedValue
  @Column(name = "commercial_voyage_id")
  private UUID commercialVoyageId;

  @Column(name = "commercial_voyage_name")
  private String commercialVoyageName;
}
