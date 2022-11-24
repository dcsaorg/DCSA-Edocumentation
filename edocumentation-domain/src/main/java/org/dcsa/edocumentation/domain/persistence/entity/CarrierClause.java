package org.dcsa.edocumentation.domain.persistence.entity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
