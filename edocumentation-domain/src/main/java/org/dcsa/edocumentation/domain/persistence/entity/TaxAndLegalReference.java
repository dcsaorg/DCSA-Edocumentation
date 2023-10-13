package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.TaxAndLegalReferenceValidation;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "tax_and_legal_reference")
@TaxAndLegalReferenceValidation(groups = AsyncShipperProvidedDataValidation.class)
public class TaxAndLegalReference {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "type_code")
  @Size(max = 50)
  @NotBlank
  private String type;

  @Column(name = "country_code")
  @Size(max = 2)
  @NotBlank
  private String countryCode;

  @Column(name = "value")
  @Size(max = 100)
  @NotBlank
  private String value;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "party_id")
  private Party party;
}
