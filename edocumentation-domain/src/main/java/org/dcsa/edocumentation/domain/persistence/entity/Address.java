package org.dcsa.edocumentation.domain.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
@Entity
@Table(name = "address")
public class Address {
  @Id
  @GeneratedValue
  @Column(name = "id", nullable = false)
  private UUID id;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "street", length = 100)
  private String street;

  @Column(name = "street_number", length = 50)
  private String streetNumber;

  @Column(name = "floor", length = 50)
  private String floor;

  @Column(name = "postal_code", length = 10)
  private String postCode;

  @Column(name = "city", length = 65)
  private String city;

  @Column(name = "state_region", length = 65)
  private String stateRegion;

  @Column(name = "country", length = 65)
  private String country;
}
