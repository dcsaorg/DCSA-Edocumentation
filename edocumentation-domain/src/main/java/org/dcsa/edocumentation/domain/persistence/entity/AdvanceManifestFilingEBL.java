package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.AdvanceManifestFilingsHouseBLPerformedBy;
import org.dcsa.edocumentation.domain.validations.AdvanceManifestFilingEBLValidation;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "advance_manifest_filing_ebl")
@AdvanceManifestFilingEBLValidation(groups = AsyncShipperProvidedDataValidation.class)
public class AdvanceManifestFilingEBL {
  @GeneratedValue
  @Column(name = "manifest_id")
  @Id private UUID manifestId;

  @Column(name = "manifest_type_code")
  @Size(max = 50)
  @NotNull
  private String manifestTypeCode;

  @Column(name = "country_code")
  @Size(max = 2)
  @NotNull
  private String countryCode;

  @Column(name = "filing_performed_by")
  @Enumerated(EnumType.STRING)
  @NotNull
  private AdvanceManifestFilingsHouseBLPerformedBy advanceManifestFilingsPerformedBy;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ShippingInstruction shippingInstruction;

  @Column(name = "self_filer_code")
  @Size(max = 100)
  private String selfFilerCode;

}
