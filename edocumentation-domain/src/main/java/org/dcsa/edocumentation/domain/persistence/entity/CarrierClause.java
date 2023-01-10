package org.dcsa.edocumentation.domain.persistence.entity;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "carrier_clauses")
public class CarrierClause {
  @Id private UUID id;

  @Column(name = "clause_content")
  private String clauseContent;

}
