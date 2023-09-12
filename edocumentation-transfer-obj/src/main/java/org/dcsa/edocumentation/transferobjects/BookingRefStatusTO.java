package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record BookingRefStatusTO(
  @NotBlank @Size(max = 100)
  String carrierBookingRequestReference,

  @NotNull
  BkgDocumentStatus documentStatus,

  List<RequestedChangeTO> requestedChanges,

  @NotNull
  OffsetDateTime bookingRequestCreatedDateTime,

  @NotNull
  OffsetDateTime bookingRequestUpdatedDateTime
) {
  @Builder(toBuilder = true)
  public BookingRefStatusTO { }
}
