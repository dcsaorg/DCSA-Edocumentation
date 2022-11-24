package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

public record CarrierClauseTO(String clauseContent) {
  @Builder(toBuilder = true)
  public CarrierClauseTO {}
}
