package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookingCancelRequestTO(
  @NotNull
  @EnumSubset(anyOf = "CANC")
  BkgDocumentStatus documentStatus,
  @Size(max = 250)
  String reason
) {
  @Builder(toBuilder = true)
  public BookingCancelRequestTO { }
}
