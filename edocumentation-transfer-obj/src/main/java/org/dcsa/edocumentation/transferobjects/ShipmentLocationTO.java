package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.Size;
import lombok.Builder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ShipmentLocationTO(
  @Valid @NotNull
  LocationTO location,

  @NotNull
  @Size(max = 3)
  String shipmentLocationTypeCode,

  OffsetDateTime eventDateTime
) {
  @Builder(toBuilder = true)
  public ShipmentLocationTO { }
}
