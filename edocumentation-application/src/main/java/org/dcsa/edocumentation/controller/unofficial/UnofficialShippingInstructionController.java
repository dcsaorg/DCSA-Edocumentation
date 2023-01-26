package org.dcsa.edocumentation.controller.unofficial;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShippingInstructionService;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ChangeEBLDocumentStatusRequestTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Emulates SMEs asynchronous validation of a ShippingInstruction.
 */
@Validated
@RestController
@RequestMapping("/unofficial${spring.application.ebl-context-path}")
@RequiredArgsConstructor
public class UnofficialShippingInstructionController {
  private final ShippingInstructionService service;

  @PostMapping(path = "/shipping-instructions/{shippingInstructionReference}/change-state")
  @ResponseStatus(HttpStatus.OK)
  public ShippingInstructionRefStatusTO changeState(
    @PathVariable("shippingInstructionReference")
    @NotBlank @Size(max = 100)
    String shippingInstructionReference,
    @Valid @RequestBody
    ChangeEBLDocumentStatusRequestTO changeEBLDocumentStatusRequestTO) {

    return service.updateShippingInstructionStatus(
      shippingInstructionReference,
      changeEBLDocumentStatusRequestTO.documentStatus(),
      changeEBLDocumentStatusRequestTO.reason()
    );
  }
}
