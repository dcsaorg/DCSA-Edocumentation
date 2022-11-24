package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ShipmentService;
import org.dcsa.edocumentation.transferobjects.ShipmentTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class ShipmentController {
  private final ShipmentService shipmentService;

  @GetMapping(
      path = "${spring.application.bkg-context-path}" + "/shipments/{carrierBookingReference}")
  @ResponseStatus(HttpStatus.OK)
  public ShipmentTO getShipment(@PathVariable @Size(max = 35) String carrierBookingReference) {
    return shipmentService.findShipment(carrierBookingReference);
  }
}
