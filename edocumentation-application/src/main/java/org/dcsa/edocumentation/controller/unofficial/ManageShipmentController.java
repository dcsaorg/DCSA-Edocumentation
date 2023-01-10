package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.ManageShipmentService;
import org.dcsa.edocumentation.transferobjects.unofficial.ManageShipmentRequestTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ShipmentRefStatusTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
public class ManageShipmentController {

  private final ManageShipmentService manageShipmentService;
  @PostMapping(
    path = "/unofficial${spring.application.bkg-context-path}/shipments")
  @ResponseStatus(HttpStatus.OK)
  public ShipmentRefStatusTO createNewShipment(@Valid @RequestBody ManageShipmentRequestTO shipmentRequestTO) {
    return manageShipmentService.create(shipmentRequestTO);
  }
}
