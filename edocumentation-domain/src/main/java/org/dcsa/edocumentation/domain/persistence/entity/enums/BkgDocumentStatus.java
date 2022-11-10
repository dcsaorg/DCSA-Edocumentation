package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum BkgDocumentStatus {
  RECE("Received", ShipmentEventTypeCode.RECE),
  PENU("Pending Update", ShipmentEventTypeCode.PENU),
  REJE("Rejected", ShipmentEventTypeCode.REJE),
  CONF("Confirmed", ShipmentEventTypeCode.CONF),
  PENC("Pending Confirmation", ShipmentEventTypeCode.PENC),
  CANC("Cancelled", ShipmentEventTypeCode.CANC),
  CMPL("Completed", ShipmentEventTypeCode.CMPL);

  @Getter
  private final String value;
  private final ShipmentEventTypeCode shipmentEventTypeCode;

  public ShipmentEventTypeCode asShipmentEventTypeCode() {
    return shipmentEventTypeCode;
  }
}
