package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.BookingService;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class BookingController {
  private final BookingService bookingService;

  @PostMapping(path = "${spring.application.bkg-context-path}/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  public BookingRefStatusTO createBooking(@Valid @RequestBody BookingTO bookingRequest) {
    return bookingService.createBooking(bookingRequest.forUpdates());
  }

  @PutMapping(path = "${spring.application.bkg-context-path}/bookings/{carrierBookingRequestReference}")
  @ResponseStatus(HttpStatus.OK)
  public BookingRefStatusTO updateBooking(
    @PathVariable("carrierBookingRequestReference")
    @NotBlank @Size(max = 100)
    String carrierBookingRequestReference,

    @Valid @RequestBody
    BookingTO bookingRequest
  ) {
    if (bookingRequest.carrierBookingRequestReference() == null || !carrierBookingRequestReference.equals(bookingRequest.carrierBookingRequestReference())) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "carrierBookingRequestReference must match bookingRequest.carrierBookingRequestReference");
    }
    return bookingService.updateBooking(carrierBookingRequestReference, bookingRequest.forUpdates());
  }

  @GetMapping(path = "${spring.application.bkg-context-path}/bookings/{carrierBookingRequestReference}")
  @ResponseStatus(HttpStatus.OK)
  public BookingTO getBooking(
    @PathVariable("carrierBookingRequestReference")
    @NotBlank @Size(max = 100)
    String carrierBookingRequestReference
  ) {
    return bookingService.getBooking(carrierBookingRequestReference);
  }
}
