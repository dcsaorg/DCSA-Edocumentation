package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;

import java.util.*;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class ConfirmedBooking {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;


}
