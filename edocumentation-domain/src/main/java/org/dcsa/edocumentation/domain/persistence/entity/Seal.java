package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "seal")
public class Seal {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "seal_number")
  @Size(max = 15)
  @NotNull
  private String sealNumber;

  @Column(name = "seal_source_code")
  @Size(max = 5)
  private String sealSource;

  @Column(name = "seal_type_code")
  @Size(max = 5)
  @NotNull
  private String sealType;
}
