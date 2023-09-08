package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "carrier")
public class Carrier {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "carrier_name", length = 100)
  private String carrierName;

  @Column(name = "smdg_code", length = 3)
  private String smdgCode;

  @Column(name = "nmfta_code", length = 4)
  private String nmftaCode;
}
