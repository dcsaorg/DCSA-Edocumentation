package org.dcsa.edocumentation.transferobjects.unofficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import org.dcsa.edocumentation.transferobjects.PartyTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DraftTransportDocumentRequestTO(


  @Size(max = 100)
  @NotBlank
  String shippingInstructionReference,

  // No issue date - the DRFT is never issued.

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate shipmentDate,

  // No carrierCode/CodeList - we pull it from issuing Party

  @NotNull
  @Valid
  PartyTO issuingParty,

  Integer numberOfRiderPages) {
}
