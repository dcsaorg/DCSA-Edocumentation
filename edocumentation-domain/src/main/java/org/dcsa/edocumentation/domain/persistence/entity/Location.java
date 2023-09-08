package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;
import org.dcsa.edocumentation.domain.validations.AsyncShipperProvidedDataValidation;
import org.dcsa.edocumentation.domain.validations.LocationValidation;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "location")
@LocationValidation(groups = AsyncShipperProvidedDataValidation.class)
public class Location {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "location_name", length = 100)
  private String locationName;

  @Column(name = "location_type", length = 100)
  private String locationType;

  @Column(name = "un_location_code", length = 5)
  private String UNLocationCode;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  private Address address;

  @Column(name = "facility_code", length = 6)
  private String facilityCode;

  @Column(name = "facility_code_list_provider", length = 4)
  private String facilityCodeListProvider;
}
