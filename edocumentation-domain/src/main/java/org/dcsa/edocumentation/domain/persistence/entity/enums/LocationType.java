package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.Getter;

@Getter
public enum LocationType {
  PRE("Place of Receipt"),
  POL("Port of Loading"),
  POD("Port of Discharge"),
  PDE("Place of Delivery"),
  PCF("Pre-carriage From"),
  PSR("Pre-carriage under shipper’s responsibility"),
  OIR("Onward In-land Routing"),
  DRL("Depot release location"),
  ORI("Origin of goods"),
  IEL("Container intermediate export stop off location"),
  PTP("Prohibited transshipment port"),
  RTP("Requested transshipment port"),
  FCD("Full container drop-off location"),
  ECP("Empty container pick-up date and time"),
  IPA("IPA Invoice payable at");
  ;

  private final String value;

  LocationType(String value) {
    this.value = value;
  }
}
