package org.dcsa.edocumentation.domain.persistence.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum IncoTerms {
  FCA("Free Carrier"),
  FOB("Free on Board");

  private final String value;
}
