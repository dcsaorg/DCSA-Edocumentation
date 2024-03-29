package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.TransportPlanStageCode;
import org.dcsa.skernel.infrastructure.validation.PseudoEnum;
import org.dcsa.skernel.infrastructure.validation.UniversalServiceReference;
import org.dcsa.skernel.infrastructure.validation.ValidVesselIMONumber;

public record TransportTO(
  @Valid @NotNull(message = "The attribute transportPlanStageCode is required.")
    TransportPlanStageCode transportPlanStage,

  @NotNull(message = "The attribute transportPlanStageSequenceNumber is required.")
    Integer transportPlanStageSequenceNumber,

  @Valid @NotNull
    LocationTO loadLocation,

  @Valid @NotNull
    LocationTO dischargeLocation,

  @NotNull(message = "The attribute plannedDepartureDate is required.")
  LocalDate plannedDepartureDate,

  @NotNull(message = "The attribute plannedArrivalDate is required.")
  LocalDate plannedArrivalDate,

  @Size(max = 10)
  @PseudoEnum(value = "modeoftransportcodes.csv", column = "DCSA Transport Type")
  String modeOfTransport,
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

  @Pattern(regexp = "^\\S+(\\s+\\S+)$")
    String carrierServiceCode,

  @UniversalServiceReference
    String universalServiceReference) {

  @Builder(toBuilder = true)
  public TransportTO {}
}
