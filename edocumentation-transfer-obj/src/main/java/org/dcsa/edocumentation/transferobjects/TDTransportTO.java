package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.DCSATransportType;
import org.dcsa.skernel.infrastructure.transferobject.LocationTO;

import java.time.LocalDate;

public record TDTransportTO(
  @NotNull
  LocalDate plannedArrivalDate,  // TODO: TransportDocumentMapper does not map this field yet
  @NotNull
  LocalDate plannedDepartureDate, // TODO: TransportDocumentMapper does not map this field yet
  DCSATransportType preCarriedBy, // TODO: TransportDocumentMapper does not map this field yet
  LocationTO placeOfReceipt,
  @NotNull
  LocationTO portOfLoading,
  @NotNull
  LocationTO portOfDischarge,
  LocationTO placeOfDelivery,
  LocationTO onwardInlandRouting
) {
  @Builder(toBuilder = true)
  public TDTransportTO {}

}
