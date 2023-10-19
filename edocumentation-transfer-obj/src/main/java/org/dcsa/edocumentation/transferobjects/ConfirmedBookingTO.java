package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

public record ConfirmedBookingTO(
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
    @NotNull @Valid BookingRequestTO booking,
    @NotNull @Valid List<TransportTO> transports,
    @Valid List<ShipmentCutOffTimeTO> shipmentCutOffTimes,
    @Valid List<ShipmentLocationTO> shipmentLocations,
    @Valid List<ConfirmedEquipmentTO> confirmedEquipments,
    @Valid List<AdvanceManifestFilingTO> advanceManifestFilings,
    @Valid List<ChargeTO> charges,
    @Valid List<CarrierClauseTO> carrierClauses) {

  @Builder(toBuilder = true)
  public ConfirmedBookingTO {}
}
