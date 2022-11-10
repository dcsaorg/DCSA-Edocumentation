package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "hs_code")
public class HsCode {

  @Id
  @Column(name = "hs_code", length = 10)
  private String hsCode;

  @Column(name = "hs_code_description", length = 250)
  private String description;

}
