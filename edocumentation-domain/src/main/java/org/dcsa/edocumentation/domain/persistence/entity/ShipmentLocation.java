package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.*;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.skernel.domain.persistence.entity.Location;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
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

  @OneToOne
  private Location location;

  @Column(name = "shipment_location_type_code")
  @Enumerated(EnumType.STRING)
  private LocationType shipmentLocationTypeCode;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @Column(name = "shipment_id")
  private UUID shipmentID;
}
