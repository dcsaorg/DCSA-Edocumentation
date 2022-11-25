package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@Table(name = "reefer_type")
public class ReeferType {
  @Id
  @Column(name = "reefer_type_code", nullable = false, length = 4)
  private String code;

  @Column(name = "reefer_type_name", nullable = false, length = 100)
  private String name;

  @Column(name = "reefer_type_description", nullable = false, length = 250)
  private String description;
}
