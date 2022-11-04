package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum VolumeUnit {
  FTQ("Cubic meter"),
  MTQ("Cubic foot")
  ;

  @Getter
  private final String value;
}
