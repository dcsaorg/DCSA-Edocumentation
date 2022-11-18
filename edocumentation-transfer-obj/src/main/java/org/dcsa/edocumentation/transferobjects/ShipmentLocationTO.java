package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.AddressLocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.FacilityLocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.UNLocationLocationTO;
import org.dcsa.skernel.infrastructure.validation.RequireType;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ShipmentLocationTO(
  @Valid @NotNull
  @RequireType(
    value = {AddressLocationTO.class, UNLocationLocationTO.class, FacilityLocationTO.class},
    message = "must be an AddressLocation, FacilityLocation or an UNLocationLocation"
  )
  LocationTO location,

  @Size(max = 250)
  String displayedName,

  @NotNull
  ShipmentLocationTypeCode shipmentLocationTypeCode,

  OffsetDateTime eventDateTime
) {
  @Builder(toBuilder = true)
  public ShipmentLocationTO { }
}
