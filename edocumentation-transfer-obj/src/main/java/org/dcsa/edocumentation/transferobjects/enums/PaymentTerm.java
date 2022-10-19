package org.dcsa.edocumentation.transferobjects.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentTerm {
  PRE("Prepaid"),
  COL("Collect");

  private final String value;
}
