package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "party_contact_details")
public class PartyContactDetails {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Size(max = 100)
  @Column(name = "name")
  private String name;

  @Size(max = 100)
  @Column(name = "email")
  private String email;

  @Size(max = 30)
  @Column(name = "phone")
  private String phone;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;
}
