package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum EblDocumentStatus {
  RECE("Received", ShipmentEventTypeCode.RECE),
  PENU("Pending Update", ShipmentEventTypeCode.PENU),
  DRFT ("Draft", ShipmentEventTypeCode.DRFT),
  PENA ("Pending Approval", ShipmentEventTypeCode.PENA),
  APPR ("Approved", ShipmentEventTypeCode.APPR),
  ISSU ("Issued", ShipmentEventTypeCode.ISSU),
  SURR ("Surrendered", ShipmentEventTypeCode.SURR),
  VOID ("Void", ShipmentEventTypeCode.VOID),
  REJE ("Rejected", ShipmentEventTypeCode.REJE),
  ;

  @Getter
  private final String value;
  private final ShipmentEventTypeCode shipmentEventTypeCode;

  public ShipmentEventTypeCode asShipmentEventTypeCode() {
    return shipmentEventTypeCode;
  }
}
