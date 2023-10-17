package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.CustomsReferenceValidation;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@CustomsReferenceValidation(groups = AsyncShipperProvidedDataValidation.class)
@Table(name = "customs_reference")
public class CustomsReference {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID referenceID;

  @Column(name = "type", nullable = false)
  @Size(max = 100)
  private String type;

  @Column(name = "country_code", nullable = false)
  @Size(min = 2, max = 2)
  @Pattern(regexp = "^[A-Z]{2}$")
  private String countryCode;

  @Column(name = "value", nullable = false)
  @Size(max = 100)
  private String value;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private UtilizedTransportEquipment utilizedTransportEquipment;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ShippingInstruction shippingInstruction;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private ConsignmentItem consignmentItem;

}
