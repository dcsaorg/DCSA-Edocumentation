package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EblDocumentStatus {
  RECE("Received"),
  PENU("Pending Update"),
  DRFT ("Draft"),
  PENA ("Pending Approval"),
  APPR ("Approved"),
  ISSU ("Issued"),
  SURR ("Surrendered"),
  VOID ("Void");

  private final String value;
}
