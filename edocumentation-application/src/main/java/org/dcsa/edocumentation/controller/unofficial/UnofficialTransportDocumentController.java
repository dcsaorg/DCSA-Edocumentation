package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.UnofficialTransportDocumentService;
import org.dcsa.edocumentation.transferobjects.TransportDocumentRefStatusTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ChangeEBLDocumentStatusRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.DraftTransportDocumentRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Emulates SMEs asynchronous validation of a ShippingInstruction.
 */
@Validated
@RestController
@RequiredArgsConstructor
public class UnofficialTransportDocumentController {
  private final UnofficialTransportDocumentService service;

  @PostMapping(path = "/unofficial${spring.application.ebl-context-path}/transport-documents")
  @ResponseStatus(HttpStatus.CREATED)
  public TransportDocumentRefStatusTO issueDraft(
    @Valid @RequestBody
    DraftTransportDocumentRequestTO draftTransportDocumentRequestTO) {

    return service.issueDraft(draftTransportDocumentRequestTO)
      .orElseThrow(() ->
        ConcreteRequestErrorMessageException.notFound(
          "No shipping instruction found with shippingInstructionReference: "
            + draftTransportDocumentRequestTO.shippingInstructionReference()));
  }

  @PostMapping(path = "/unofficial${spring.application.ebl-context-path}/transport-documents/{transportDocumentReference}/change-state")
  @ResponseStatus(HttpStatus.OK)
  public TransportDocumentRefStatusTO changeState(
    @PathVariable("transportDocumentReference")
    @NotBlank @Size(max = 20)
    String transportDocumentReference,
    @Valid @RequestBody
    ChangeEBLDocumentStatusRequestTO changeEBLDocumentStatusRequestTO) {

    return service.changeState(
        transportDocumentReference,
        changeEBLDocumentStatusRequestTO.documentStatus()
      ).orElseThrow(() ->
        ConcreteRequestErrorMessageException.notFound(
          "No transport document with transportDocumentReference: "
            + transportDocumentReference));
  }
}
