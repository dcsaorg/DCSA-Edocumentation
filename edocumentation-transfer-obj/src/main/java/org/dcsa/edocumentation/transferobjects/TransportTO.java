package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStage;
import org.dcsa.skernel.infrastructure.validation.RequireType;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;

public record TransportTO(
    @Valid @NotNull(message = "The attribute transportPlanStage is required.")
        TransportPlanStage transportPlanStage,
    @NotNull(message = "The attribute transportPlanStageSequenceNumber is required.")
        Integer transportPlanStageSequenceNumber,
    @Valid
        @NotNull
        @RequireType(
            value = {
              LocationTO.AddressLocationTO.class,
              LocationTO.UNLocationLocationTO.class,
              LocationTO.FacilityLocationTO.class
            },
            message = "must be an AddressLocation, FacilityLocation or an UNLocationLocation")
        LocationTO loadLocation,
    @Valid
        @NotNull
        @RequireType(
            value = {
              LocationTO.AddressLocationTO.class,
              LocationTO.UNLocationLocationTO.class,
              LocationTO.FacilityLocationTO.class
            },
            message = "must be an AddressLocation, FacilityLocation or an UNLocationLocation")
        LocationTO dischargeLocation,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute plannedDepartureDate is required.")
        OffsetDateTime plannedDepartureDate,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
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