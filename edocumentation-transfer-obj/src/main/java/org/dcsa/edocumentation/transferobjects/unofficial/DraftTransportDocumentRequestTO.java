package org.dcsa.edocumentation.transferobjects.unofficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.dcsa.edocumentation.transferobjects.PartyTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public record DraftTransportDocumentRequestTO(


  @Size(max = 100)
  @NotBlank
  String shippingInstructionReference,

  // No issue date - the DRFT is never issued.

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate shippedOnBoardDate,

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate receivedForShipmentDate,

  Integer numberOfOriginalsWithCharges,

  Integer numberOfOriginalsWithoutCharges,

  // No carrierCode/CodeList - we pull it from issuing Party

  @NotNull
  PartyTO issuingParty,

  Integer numberOfRiderPages) {
}
