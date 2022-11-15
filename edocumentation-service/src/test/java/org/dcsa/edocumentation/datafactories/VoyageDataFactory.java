package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;

import java.util.UUID;

@UtilityClass
public class VoyageDataFactory {
  public static Voyage voyage() {
    return Voyage.builder()
      .id(UUID.fromString("a2698d81-75fd-4ed5-ae8e-e5881d4b84c8"))
      .carrierVoyageNumber("voyageRef")
      .universalVoyageReference("12345")
      .build();
  }
}
