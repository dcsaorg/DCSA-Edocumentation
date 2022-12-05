package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.service.unofficial.UnofficialShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.ChangeShippingInstructionStatusRequestTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
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
public class UnofficialShippingInstructionController {
  private final UnofficialShippingInstructionService service;

  @PostMapping(path = "/unofficial${spring.application.ebl-context-path}/shipping-instructions/{shippingInstructionReference}/change-state")
  @ResponseStatus(HttpStatus.OK)
  public ShippingInstructionRefStatusTO changeState(
    @PathVariable("shippingInstructionReference")
    @NotBlank @Size(max = 100)
    String shippingInstructionReference,
    @Valid @RequestBody
    ChangeShippingInstructionStatusRequestTO changeShippingInstructionStatusRequestTO) {

    return service.changeState(shippingInstructionReference,
        changeShippingInstructionStatusRequestTO.documentStatus(),
        changeShippingInstructionStatusRequestTO.reason())
      .orElseThrow(() ->
        ConcreteRequestErrorMessageException.notFound(
          "No shipping instruction found with shippingInstructionReference: "
            + shippingInstructionReference));
  }
}