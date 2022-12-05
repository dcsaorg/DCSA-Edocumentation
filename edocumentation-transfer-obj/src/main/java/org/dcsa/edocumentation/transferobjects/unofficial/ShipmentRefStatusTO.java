package org.dcsa.edocumentation.transferobjects.unofficial;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ShipmentRefStatusTO(
  @NotBlank @Size(max = 35)
  String carrierBookingReference,

  @NotNull
  BkgDocumentStatus documentStatus,

  @NotNull
  OffsetDateTime bookingRequestCreatedDateTime,

  @NotNull
  OffsetDateTime bookingRequestUpdatedDateTime
) {
  @Builder(toBuilder = true)
  public ShipmentRefStatusTO { }
}
