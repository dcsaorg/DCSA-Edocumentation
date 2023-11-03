package org.dcsa.edocumentation.controller.unofficial;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.ManageShipmentService;
import org.dcsa.edocumentation.transferobjects.unofficial.ConfirmedBookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.unofficial.ManageConfirmedBookingRequestTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
public class ManageConfirmedBookingController {

  private final ManageShipmentService manageShipmentService;
  @PostMapping(
    path = "/unofficial${spring.application.bkg-context-path}/confirmed-bookings")
  @ResponseStatus(HttpStatus.OK)
  public ConfirmedBookingRefStatusTO createNewConfirmedBooking(@Valid @RequestBody ManageConfirmedBookingRequestTO shipmentRequestTO) {
    return manageShipmentService.create(shipmentRequestTO);
  }
}
