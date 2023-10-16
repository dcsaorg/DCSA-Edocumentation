package org.dcsa.edocumentation.transferobjects.unofficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import org.dcsa.edocumentation.transferobjects.PartyTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;
import org.dcsa.skernel.infrastructure.validation.PseudoEnum;

import java.time.LocalDate;

@AllOrNone({"declaredValueCurrency", "declaredValue"})
public record DraftTransportDocumentRequestTO(


  @Size(max = 100)
  @NotBlank
  String shippingInstructionReference,

  // No issue date - the EblDocumentStatus.DRAFT is never issued.

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  LocalDate shipmentDate,

  // No carrierCode/CodeList - we pull it from issuing Party

  @NotNull
  @Valid
  PartyTO issuingParty,

  Integer numberOfRiderPages,

  @PseudoEnum(value = "currencycodes.csv")
  String declaredValueCurrency,

  Float declaredValue) {
}
