package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "outer_packaging")
public class OuterPackaging {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "package_code")
  private String packageCode;

  @Column(name = "imo_packaging_code")
  private String imoPackagingCode;

  @Column(name = "number_of_packages")
  private Integer numberOfPackages;

  @Column(name = "description", length = 100)
  private String description;

}
