package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import javax.validation.constraints.Size;

public record AddressTO(
  @Size(max = 100) String name,
  @Size(max = 100) String street,
  @Size(max = 50) String streetNumber,
  @Size(max = 50) String floor,
  @Size(max = 50) String postCode,
  @Size(max = 65) String city,
  @Size(max = 65) String stateRegion,
  @Size(max = 75) String country
) {
  @Builder(toBuilder = true)
  public AddressTO { }
}
