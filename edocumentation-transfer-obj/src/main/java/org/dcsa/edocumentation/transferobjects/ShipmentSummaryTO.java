package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ShipmentSummaryTO(
    @NotBlank(message = "The attribute carrierBookingReference is required.")
        @Size(max = 35, message = "The attribute carrierBookingReference has a max size of 35.")
        String carrierBookingReference,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute shipmentCreatedDateTime is required.")
        OffsetDateTime shipmentCreatedDateTime,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        OffsetDateTime shipmentUpdatedDateTime,
    String carrierBookingRequestReference,
    String termsAndConditions,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) BkgDocumentStatus documentStatus) {
  @Builder
  public ShipmentSummaryTO {}
}
