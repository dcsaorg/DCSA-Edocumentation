package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Builder;

public record TDTransportTO(
  @NotNull
  LocalDate plannedArrivalDate,  // TODO: TransportDocumentMapper does not map this field yet
  @NotNull
  LocalDate plannedDepartureDate, // TODO: TransportDocumentMapper does not map this field yet
  @Size(max = 10)
  String preCarriedBy,  // TODO: TransportDocumentMapper does not map this field yet
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
