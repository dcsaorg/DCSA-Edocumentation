package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "tax_and_legal_reference")
public class TaxAndLegalReference {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID taxAndLegalReferenceID;

  @Column(name = "tax_and_legal_reference_type_code")
  @Size(max = 50)
  @NotBlank
  private String type;

  @Column(name = "tax_and_legal_reference_country_code")
  @Size(max = 2)
  @NotBlank
  private String countryCode;

  @Column(name = "tax_and_legal_reference_value")
  @Size(max = 100)
  @NotBlank
  private String value;

//  @JoinColumn(name = "party_id")
//  @Column(name = "party_id")
//  private UUID partyID;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private Party party;
}
