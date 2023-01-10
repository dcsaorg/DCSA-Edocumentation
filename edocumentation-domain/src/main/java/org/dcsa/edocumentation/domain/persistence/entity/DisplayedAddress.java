package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "displayed_address")
public class DisplayedAddress {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 35)
  @Column(name = "address_line_1")
  private String addressLine1;

  @Size(max = 35)
  @Column(name = "address_line_2")
  private String addressLine2;

  @Size(max = 35)
  @Column(name = "address_line_3")
  private String addressLine3;

  @Size(max = 35)
  @Column(name = "address_line_4")
  private String addressLine4;

  @Size(max = 35)
  @Column(name = "address_line_5")
  private String addressLine5;
}
