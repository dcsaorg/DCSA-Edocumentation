package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.UnofficialShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.unofficial.ChangeEBLDocumentStatusRequestTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
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
public class UnofficialShippingInstructionController {
  private final UnofficialShippingInstructionService service;

  @PostMapping(path = "/unofficial${spring.application.ebl-context-path}/shipping-instructions/{shippingInstructionReference}/change-state")
  @ResponseStatus(HttpStatus.OK)
  public ShippingInstructionRefStatusTO changeState(
    @PathVariable("shippingInstructionReference")
    @NotBlank @Size(max = 100)
    String shippingInstructionReference,
    @Valid @RequestBody
    ChangeEBLDocumentStatusRequestTO changeEBLDocumentStatusRequestTO) {

    return service.changeState(shippingInstructionReference,
        changeEBLDocumentStatusRequestTO.documentStatus(),
        changeEBLDocumentStatusRequestTO.reason())
      .orElseThrow(() ->
        ConcreteRequestErrorMessageException.notFound(
          "No shipping instruction found with shippingInstructionReference: "
            + shippingInstructionReference));
  }

  @PostMapping(path = "/unofficial${spring.application.ebl-context-path}/shipping-instructions/{shippingInstructionReference}/validate")
  @ResponseStatus(HttpStatus.OK)
  public ShippingInstructionRefStatusTO validate(
    @PathVariable("shippingInstructionReference")
    @NotBlank @Size(max = 100)
    String shippingInstructionReference
    ) {

    return service.validateShippingInstruction(shippingInstructionReference)
      .orElseThrow(() ->
        ConcreteRequestErrorMessageException.notFound(
          "No shipping instruction found with shippingInstructionReference: "
            + shippingInstructionReference));
  }
}
