package org.dcsa.edocumentation.transferobjects.unofficial;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ShipmentRefStatusTO(
  @NotBlank @Size(max = 35)
  String carrierBookingReference,

  @NotNull
  String bookingStatus,

  @NotNull
  OffsetDateTime bookingRequestCreatedDateTime,

  @NotNull
  OffsetDateTime bookingRequestUpdatedDateTime
) {
  @Builder(toBuilder = true)
  public ShipmentRefStatusTO { }
}
