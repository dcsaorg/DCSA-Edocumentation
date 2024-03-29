package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "advance_manifest_filing")
public class AdvanceManifestFiling {
  @GeneratedValue
  @Id private UUID manifest_id;
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "confirmed_booking_id")
  @Setter(AccessLevel.PACKAGE)
  private ConfirmedBooking confirmedBooking;

  @Column(name = "manifest_type_code")
  private String manifestTypeCode;

  @Column(name = "country_code")
  private String countryCode;

}
