package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record ShipmentTO(
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
    String termsAndConditions,
    @Valid BookingTO booking,
    @Valid List<TransportTO> transports,
    @Valid List<ShipmentCutOffTime> shipmentCutOffTimes,
    @Valid List<ShipmentLocationTO> shipmentLocations,
    @Valid List<ConfirmedEquipment> confirmedEquipments,
    @Valid List<Charge> charges,
    @Valid List<CarrierClause> carrierClauses) {

  @Builder(toBuilder = true)
  public ShipmentTO {}
}
