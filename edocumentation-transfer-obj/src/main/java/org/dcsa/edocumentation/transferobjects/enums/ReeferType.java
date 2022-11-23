package org.dcsa.edocumentation.transferobjects.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public enum ReeferType {
  FREZ("Freezer"),
  SUPR("Super Freezer"),
  REFR("Refrigerated"),
  CONA("Controlled Atmosphere");

  private final String value;
}
