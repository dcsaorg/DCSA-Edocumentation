package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BkgDocumentStatus {
  RECE("Received"),
  PENU("Pending Update"),
  REJE("Rejected"),
  CONF("Confirmed"),
  PENC("Pending Confirmation"),
  CANC("Cancelled"),
  CMPL("Completed");

  private final String value;
}
