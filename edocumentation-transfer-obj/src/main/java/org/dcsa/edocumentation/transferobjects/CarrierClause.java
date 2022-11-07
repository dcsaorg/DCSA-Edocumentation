package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

public record CarrierClause(String clauseContent) {
  @Builder(toBuilder = true)
  public CarrierClause {}
}
