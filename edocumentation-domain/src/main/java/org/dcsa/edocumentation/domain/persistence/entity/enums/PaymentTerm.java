package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentTerm {
  PRE("Prepaid"),
  COL("Collect");

  private final String value;
}
