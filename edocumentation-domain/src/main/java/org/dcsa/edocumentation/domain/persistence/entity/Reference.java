package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReferenceTypeCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Builder
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

  // ToDo only the required associations for booking requests have been implemented

}
