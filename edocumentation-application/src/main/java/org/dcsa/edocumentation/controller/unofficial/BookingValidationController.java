package org.dcsa.edocumentation.controller.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.unofficial.BookingValidationService;
import org.dcsa.edocumentation.service.unofficial.BookingValidationService.ValidationResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Emulates SMEs asynchronous validation of a booking.
 */
@Validated
@RestController
@RequiredArgsConstructor
public class BookingValidationController {
  private final BookingValidationService bookingValidationService;

  @PostMapping(path = "/unofficial${spring.application.bkg-context-path}/bookings/{carrierBookingRequestReference}/validate")
  @ResponseStatus(HttpStatus.OK)
  public ValidationResult validateBooking(
    @PathVariable("carrierBookingRequestReference")
    @NotBlank @Size(max = 100)
    String carrierBookingRequestReference) {
    return bookingValidationService.validateBooking(carrierBookingRequestReference);
  }
}
