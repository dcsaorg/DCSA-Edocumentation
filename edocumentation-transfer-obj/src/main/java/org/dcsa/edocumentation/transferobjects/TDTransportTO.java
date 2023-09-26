package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;

public record TDTransportTO(
  @NotNull
  LocalDate plannedArrivalDate,
  @NotNull
  LocalDate plannedDepartureDate,
  @Size(max = 10)
  String preCarriageBy,
  @Size(max = 10)
  String onCarriageBy,
  LocationTO placeOfReceipt,
  @NotNull
  LocationTO portOfLoading,
  @NotNull
  LocationTO portOfDischarge,
  LocationTO placeOfDelivery,
  LocationTO onwardInlandRouting,

  @NotNull
  String vesselName,
  @NotNull
  String carrierExportVoyageNumber,
  String universalExportVoyageReference
) {
  @Builder(toBuilder = true)
  public TDTransportTO {}

}
