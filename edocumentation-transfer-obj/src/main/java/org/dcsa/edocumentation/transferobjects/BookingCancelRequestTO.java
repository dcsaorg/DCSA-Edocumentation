package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.EnumSubset;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookingCancelRequestTO(
  @NotNull
  @EnumSubset(anyOf = "CANCELLED,REJECTED,DECLINED")
  String bookingStatus,
  @Size(max = 250)
  String reason
) {
  @Builder(toBuilder = true)
  public BookingCancelRequestTO { }
}
