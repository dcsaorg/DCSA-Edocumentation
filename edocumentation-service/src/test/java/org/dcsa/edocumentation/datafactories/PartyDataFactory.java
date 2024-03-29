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
      .taxReference1("1234567890")
      .publicKey("""
        -----BEGIN RSA PUBLIC KEY-----
        MEgCQQCo9+BpMRYQ/dL3DS2CyJxRF+j6ctbT3/Qp84+KeFhnii7NT7fELilKUSnx
        S30WAvQCCo2yU1orfgqr41mM70MBAgMBAAE=
        -----END RSA PUBLIC KEY-----
        """)
      .address(AddressDataFactory.getSingleAddress())
      .partyContactDetails(Set.of(PartyContactDetails.builder()
          .id(UUID.randomUUID())
          .email("info@dcsa.org")
        .build()))
      .build();
  }

}
