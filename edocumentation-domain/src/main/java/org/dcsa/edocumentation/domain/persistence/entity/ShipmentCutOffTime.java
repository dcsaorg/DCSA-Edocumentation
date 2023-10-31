package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "shipment_cutoff_time")
public class ShipmentCutOffTime {

  @Id
  @GeneratedValue
  private UUID id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "booking_data_id")
  // Used by confirmed-booking to make the JPA relations work
  @Setter(AccessLevel.PACKAGE)
  private BookingData bookingData;

  @Column(name = "cut_off_time_code")
  private String cutOffDateTimeCode;

  @Column(name = "cut_off_time", length = 35)
  private OffsetDateTime cutOffDateTime;

}
