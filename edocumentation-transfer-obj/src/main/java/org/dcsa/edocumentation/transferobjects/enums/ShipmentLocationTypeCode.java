package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ShipmentLocationTypeCode {
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
  ECP("Empty container pick-up date and time")
  ;

  @Getter
  private final String value;
}
