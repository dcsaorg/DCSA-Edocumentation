package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
