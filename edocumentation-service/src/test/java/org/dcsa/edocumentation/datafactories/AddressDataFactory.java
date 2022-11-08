package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.skernel.domain.persistence.entity.Address;

import java.util.UUID;

@UtilityClass
public class AddressDataFactory {
  public Address getSingleAddress() {
    return Address.builder()
        .id(UUID.randomUUID())
        .name("DCSA Address")
        .country("Netherlands")
        .city("Amsterdam")
        .stateRegion("Noord-Holland")
        .postCode("1077 ZX")
        .street("Strawingskylaan")
        .streetNumber("4117")
        .floor("6")
        .build();
  }
}
