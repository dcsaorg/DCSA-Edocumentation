package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
  private String number;

  @Column(name = "seal_source_code")
  @Size(max = 5)
  private String source;

  @Column(name = "seal_type_code")
  @Size(max = 5)
  @NotNull
  private String type;
}
