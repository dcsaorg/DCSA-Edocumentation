package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.validation.Valid;
import lombok.*;

import jakarta.persistence.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PartyValidation;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "party")
@PartyValidation(groups = AsyncShipperProvidedDataValidation.class)
public class Party {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "party_name", length = 100)
  private String partyName;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "party")
  private Set<@Valid PartyContactDetails> partyContactDetails;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OrderColumn(name = "list_order")
  @OneToMany(mappedBy = "party")
  private Set<@Valid TaxAndLegalReference> taxAndLegalReferences;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "party")
  private Set<@Valid PartyIdentifyingCode> identifyingCodes;
}
