package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.Getter;

public enum ValueAddedServiceCode {
  SCON("Smart containers"),
  CINS("Cargo insurance"),
  SIOT("Smart IoT devices"),
  CDECL("Customs declaration"),
  SGUAR("Shipping guarantee"),
  UPPY("Upfront payment");

  ValueAddedServiceCode(String value) {
    this.value = value;
  }

  @Getter private final String value;
}
