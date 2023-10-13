package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.PartyContactDetails;

import java.util.Set;
import java.util.UUID;

@UtilityClass
public class PartyDataFactory {

  public Party singleParty() {
    return Party.builder()
      .id(UUID.randomUUID())
      .partyName("DCSA")
      .address(AddressDataFactory.getSingleAddress())
      .partyContactDetails(Set.of(PartyContactDetails.builder()
          .id(UUID.randomUUID())
          .email("info@dcsa.org")
        .build()))
      .build();
  }

}
