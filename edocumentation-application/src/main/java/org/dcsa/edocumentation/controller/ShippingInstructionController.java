package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class ShippingInstructionController {

  private final ShippingInstructionService service;

  @GetMapping(
      path =
          "${spring.application.ebl-context-path}/shipping-instructions/{shippingInstructionReference}")
  @ResponseStatus(HttpStatus.OK)
  ShippingInstructionTO getShippingInstructionByReference(
      @PathVariable("shippingInstructionReference") @NotBlank @Size(max = 100)
          String shippingInstructionReference) {

    return service
        .findByReference(shippingInstructionReference)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No shipping instruction found with shippingInstructionReference: "
                        + shippingInstructionReference));
  }

  @PostMapping(path = "${spring.application.ebl-context-path}/shipping-instructions")
  @ResponseStatus(HttpStatus.CREATED)
  ShippingInstructionRefStatusTO createShippingInstruction(
      @Valid @RequestBody ShippingInstructionTO shippingInstructionTO) {
    return service.createShippingInstruction(shippingInstructionTO);
  }
}
