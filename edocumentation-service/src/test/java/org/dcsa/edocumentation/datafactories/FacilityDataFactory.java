package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.skernel.domain.persistence.entity.Facility;

import java.util.UUID;

@UtilityClass
public class FacilityDataFactory {
  public static final String NAME = "PATRICK SYDNEY AUTOSTRAD TERMINAL";
  public static final String UNLOCATION_CODE = "AUSYD";
  public static final String SMDG_CODE = "ASLPB";

  public static Facility facility() {
    return Facility.builder()
      .id(UUID.fromString("8a92fb86-000d-498a-89e2-c3422760d018"))
      .facilityName(NAME)
      .UNLocationCode(UNLOCATION_CODE)
      .facilitySMDGCode(SMDG_CODE)
      .build();
  }
}
