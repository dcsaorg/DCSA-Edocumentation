package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReferenceTypeCode;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

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
  @Enumerated(EnumType.STRING)
  private ReferenceTypeCode type;

  @Column(name = "reference_value")
  @Size(max = 100)
  @NotNull
  private String value;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @JoinColumn(name = "shipping_instruction_id")
  @Column(name = "shipping_instruction_id")
  private UUID shippingInstructionID;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "consignment_item_id")
  private ConsignmentItem consignmentItem;

}
