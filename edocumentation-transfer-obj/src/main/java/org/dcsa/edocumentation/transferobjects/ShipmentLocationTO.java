package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.LocationType;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ShipmentLocationTO(
  @Valid @NotNull
  @RestrictLocationTO({LocationType.ADDRESS, LocationType.UNLOCATION, LocationType.FACILITY})
  LocationTO location,

  @NotNull
  ShipmentLocationTypeCode shipmentLocationTypeCode,

  OffsetDateTime eventDateTime
) {
  @Builder(toBuilder = true)
  public ShipmentLocationTO { }
}
