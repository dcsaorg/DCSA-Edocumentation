package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.skernel.domain.persistence.entity.Location;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_location")
public class ShipmentLocation {

  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @OneToOne private Location location;

  @Column(name = "shipment_location_type_code")
  @Enumerated(EnumType.STRING)
  private LocationType shipmentLocationTypeCode;

  @Column(name = "displayed_name")
  private String displayedName;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  // ToDo only the required associations for booking summaries and booking request have been
  // implemented

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;
}
