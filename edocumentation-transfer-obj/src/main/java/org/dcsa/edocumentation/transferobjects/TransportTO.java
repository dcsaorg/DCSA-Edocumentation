package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO.LocationType;
import org.dcsa.skernel.infrastructure.validation.RestrictLocationTO;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

public record TransportTO(
    @Valid @NotNull(message = "The attribute transportPlanStageCode is required.")
    TransportPlanStageCode transportPlanStage,

    @NotNull(message = "The attribute transportPlanStageSequenceNumber is required.")
    Integer transportPlanStageSequenceNumber,

    @Valid @NotNull
    @RestrictLocationTO({LocationType.ADDRESS, LocationType.UNLOCATION, LocationType.FACILITY})
    LocationTO loadLocation,

    @Valid @NotNull
    @RestrictLocationTO({LocationType.ADDRESS, LocationType.UNLOCATION, LocationType.FACILITY})
    LocationTO dischargeLocation,

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "The attribute plannedDepartureDate is required.")
        OffsetDateTime plannedDepartureDate,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "The attribute plannedArrivalDate is required.")
    OffsetDateTime plannedArrivalDate,
    DCSATransportType modeOfTransport,
    @Size(max = 35, message = "The attribute vesselName has a max size of 35.") String vesselName,
    @ValidVesselIMONumber(allowNull = true, message = "The attribute vesselIMONumber is invalid.")
        String vesselIMONumber,
    @Size(max = 50, message = "The attribute carrierImportVoyageNumber has a max size of 50.")
        String carrierImportVoyageNumber,
    @Pattern(regexp = "\\d{2}[0-9A-Z]{2}[NEWS]", message = "Not a valid voyage reference")
        String universalImportVoyageReference,
    @Size(max = 50, message = "The attribute carrierExportVoyageNumber has a max size of 50.")
        String carrierExportVoyageNumber,
    @Pattern(regexp = "\\d{2}[0-9A-Z]{2}[NEWS]", message = "Not a valid voyage reference")
        String universalExportVoyageReference,
    boolean isUnderShippersResponsibility) {
  @Builder(toBuilder = true)
  public TransportTO {}
}
