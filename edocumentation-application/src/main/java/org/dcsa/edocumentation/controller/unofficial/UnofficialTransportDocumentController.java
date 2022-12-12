package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.UnofficialShippingInstructionService;
import org.dcsa.edocumentation.service.unofficial.UnofficialTransportDocumentService;
import org.dcsa.edocumentation.transferobjects.ChangeShippingInstructionStatusRequestTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.unofficial.DraftTransportDocumentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.TransportDocumentRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
}
