package org.dcsa.edocumentation.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("${spring.application.ebl-context-path}")
@RequiredArgsConstructor
public class ShippingInstructionController {

  private final ShippingInstructionService service;

  @GetMapping(path = "/shipping-instructions/{shippingInstructionReference}")
  @ResponseStatus(HttpStatus.OK)
  ShippingInstructionTO getShippingInstructionByReference(@PathVariable("shippingInstructionReference") @NotBlank @Size(max = 100) String shippingInstructionReference) {
    return service.findByReference(shippingInstructionReference);
  }

  @PostMapping(path = "/shipping-instructions")
  @ResponseStatus(HttpStatus.CREATED)
  ShippingInstructionRefStatusTO createShippingInstruction(@Valid @RequestBody ShippingInstructionTO shippingInstructionTO) {
    if (shippingInstructionTO.shippingInstructionReference() != null
      || shippingInstructionTO.documentStatus() != null
      || shippingInstructionTO.shippingInstructionCreatedDateTime() != null
      || shippingInstructionTO.shippingInstructionUpdatedDateTime() != null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "shippingInstructionReference, documentStatus, shippingInstructionCreatedDateTime and"
          + " shippingInstructionUpdatedDateTime are not allowed when creating a shipping instruction");
    }

    return service.createShippingInstruction(shippingInstructionTO);
  }
}
