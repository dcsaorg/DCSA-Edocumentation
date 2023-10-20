package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.ConfirmedBookingService;
import org.dcsa.edocumentation.transferobjects.ConfirmedBookingTO;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class ConfirmedBookingController {
  private final ConfirmedBookingService confirmedBookingService;

  @GetMapping(
      path = "${spring.application.bkg-context-path}" + "/confirmed-bookings/{carrierBookingReference}")
  @ResponseStatus(HttpStatus.OK)
  public ConfirmedBookingTO getConfirmedBooking(@PathVariable @Size(max = 35) String carrierBookingReference) {
    return confirmedBookingService.findConfirmedBooking(carrierBookingReference);
  }
}
