package org.dcsa.edocumentation.controller.unofficial;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.UnofficialBookingRequestService;
import org.dcsa.edocumentation.transferobjects.unofficial.BookingCancelRequestTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Emulates SMEs asynchronous validation of a booking.
 */
@Validated
@RestController
@RequiredArgsConstructor
public class UnofficialBookingRequestController {
  private final UnofficialBookingRequestService unofficialBookingRequestService;

  @PostMapping(path = "/unofficial${spring.application.bkg-context-path}/booking-requests/{carrierBookingRequestReference}/validate")
  @ResponseStatus(HttpStatus.OK)
  public BookingRequestRefStatusTO validateBooking(
    @PathVariable("carrierBookingRequestReference")
    @NotBlank @Size(max = 100)
    String carrierBookingRequestReference) {
    return unofficialBookingRequestService.performBookingValidation(carrierBookingRequestReference);
  }

  @PatchMapping(
    path = "/unofficial${spring.application.bkg-context-path}/booking-requests/{carrierBookingRequestReference}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public BookingRequestRefStatusTO cancelBooking(
    @Valid @PathVariable("carrierBookingRequestReference") @NotNull @Size(max = 100)
    String carrierBookingRequestReference,
    @Valid @RequestBody BookingCancelRequestTO bookingCancelRequestTO) {
    return unofficialBookingRequestService.cancelBooking(carrierBookingRequestReference, bookingCancelRequestTO)
      .orElseThrow(
        () ->
          ConcreteRequestErrorMessageException.notFound(
            "No booking request found with carrierBookingRequestReference: "
              + carrierBookingRequestReference));
  }
}
