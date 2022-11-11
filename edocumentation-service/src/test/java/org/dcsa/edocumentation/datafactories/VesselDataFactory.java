package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.enums.VesselType;
import org.dcsa.skernel.domain.persistence.entity.enums.DimensionUnit;

import java.util.UUID;

@UtilityClass
public class VesselDataFactory {
  public static Vessel vessel() {
    return Vessel.builder()
      .id(UUID.fromString("f90de80b-af26-4703-93b0-722bb3fa69c7"))
      .vesselIMONumber("1234567")
      .name("Mock Vessel")
      .flag("NL")
      .callSignNumber("123456789012345678")
      .isDummy(false)
      .length(120F)
      .width(40F)
      .dimensionUnit(DimensionUnit.MTR)
      .type(VesselType.CONT)
      .build();
  }
}
