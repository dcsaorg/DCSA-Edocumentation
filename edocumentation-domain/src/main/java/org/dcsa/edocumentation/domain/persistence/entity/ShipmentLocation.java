package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.LocationSubType;
import org.dcsa.edocumentation.domain.validations.LocationValidation;
import org.dcsa.edocumentation.domain.validations.PseudoEnum;

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

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @LocationValidation(
    allowedSubtypes = {LocationSubType.ADDR, LocationSubType.UNLO, LocationSubType.FACI},
    groups = AsyncShipperProvidedDataValidation.class
  )
  private Location location;

  @Column(name = "shipment_location_type_code")
  @PseudoEnum(value = "shipmentlocationtypes.csv", groups = AsyncShipperProvidedDataValidation.class)
  private String shipmentLocationTypeCode;

  @Column(name = "event_date_time")
  private OffsetDateTime eventDateTime;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_id")
  private Booking booking;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "confirmed_booking_id")
  private ConfirmedBooking confirmedBooking;
}
