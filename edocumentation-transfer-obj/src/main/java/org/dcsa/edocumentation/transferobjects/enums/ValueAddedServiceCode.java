package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ValueAddedServiceCode {
  SCON("Smart containers"),
  CINS("Cargo insurance"),
  SIOT("Smart IoT devices"),
  CDECL("Customs declaration"),
  SGUAR("Shipping guarantee"),
  UPPY("Upfront payment")
  ;

  @Getter
  private final String value;
}
