package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;

import jakarta.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "mode_of_transport")
public class ModeOfTransport {

  @Id
  @Column(name = "mode_of_transport_code", length = 3)
  private String code;

  @Column(name = "mode_of_transport_name", length = 100)
  private String name;

  @Column(name = "mode_of_transport_description", length = 250)
  private String description;

  @Column(name = "dcsa_transport_type", length = 50, unique = true)
  @Enumerated(EnumType.STRING)
  private DCSATransportType dcsaTransportType;
}
