package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import org.dcsa.edocumentation.domain.validations.*;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "document_party")
@DocumentPartyEBLValidation(groups = EBLValidation.class)
@DocumentPartyPaperBLValidation(groups = PaperBLValidation.class)
public class DocumentParty {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @OneToOne
  private Party party;

  @PseudoEnum(value = "partyfunctioncodes.csv", groups = AsyncShipperProvidedDataValidation.class)
  @Column(name = "party_function")
  private String partyFunction;

  @Column(name = "is_to_be_notified")
  private Boolean isToBeNotified = false;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToOne(cascade = CascadeType.ALL)
  private DisplayedAddress displayedAddress;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @JoinColumn(name = "shipping_instruction_id")
  @Column(name = "shipping_instruction_id")
  private UUID shippingInstructionID;

}
