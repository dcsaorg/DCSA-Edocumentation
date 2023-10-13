package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;
import org.dcsa.skernel.infrastructure.validation.PseudoEnum;

public record TDTransportTO(
  @NotNull
  LocalDate plannedArrivalDate,
  @NotNull
  LocalDate plannedDepartureDate,
  @Size(max = 50)
  @PseudoEnum(value = "modeoftransportcodes.csv", column = "DCSA Transport Type")
  String preCarriageBy,
  @Size(max = 50)
  @PseudoEnum(value = "modeoftransportcodes.csv", column = "DCSA Transport Type")
  String onCarriageBy,
  LocationTO placeOfReceipt,
  @NotNull
  LocationTO portOfLoading,
  @NotNull
  LocationTO portOfDischarge,
  LocationTO placeOfDelivery,
  LocationTO onwardInlandRouting,
  @Size(max = 35)
  @NotNull
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String vesselName,
  @NotNull
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  @Size(max = 50)
  String carrierExportVoyageNumber,
  @Pattern(regexp = "^\\d{2}[0-9A-Z]{2}[NEWSR]$")
  String universalExportVoyageReference
) {
  @Builder(toBuilder = true)
  public TDTransportTO {}

}
