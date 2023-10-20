package org.dcsa.edocumentation.transferobjects;


import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record BookingRequestRefStatusTO(
  @NotBlank @Size(max = 100)
  String carrierBookingRequestReference,

  @NotNull
  String bookingStatus,

  List<RequestedChangeTO> requestedChanges,

  @NotNull
  OffsetDateTime bookingRequestCreatedDateTime,

  @NotNull
  OffsetDateTime bookingRequestUpdatedDateTime
) {
  @Builder(toBuilder = true)
  public BookingRequestRefStatusTO { }
}
