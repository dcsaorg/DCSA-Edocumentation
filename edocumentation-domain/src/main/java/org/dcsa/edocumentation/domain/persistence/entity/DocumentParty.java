package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.PartyFunction;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "document_party")
public class DocumentParty {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @OneToOne
  private Party party;

  @Column(name = "party_function")
  @Enumerated(EnumType.STRING)
  private PartyFunction partyFunction;

  @Column(name = "is_to_be_notified")
  private Boolean isToBeNotified = false;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "documentParty")
  private Set<DisplayedAddress> displayedAddress;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  // ToDo only the required associations for booking summaries and booking request have been
  // implemented
}
