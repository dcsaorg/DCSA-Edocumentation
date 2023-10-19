package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "reference")
public class Reference {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID referenceID;

  @Column(name = "reference_type_code")
  @NotNull
  @PseudoEnum(value = "referencetypes.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String type;

  @Column(name = "reference_value")
  @Size(max = 100)
  @NotNull
  private String value;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_request_id")
  private BookingRequest bookingRequest;

  @JoinColumn(name = "shipping_instruction_id")
  @Column(name = "shipping_instruction_id")
  private UUID shippingInstructionID;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "consignment_item_id")
  private ConsignmentItem consignmentItem;

}
