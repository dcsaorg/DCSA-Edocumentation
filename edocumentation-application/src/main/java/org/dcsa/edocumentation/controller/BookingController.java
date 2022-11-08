package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.BookingService;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class BookingController {
  private final BookingService bookingService;

  @PostMapping(path = "${spring.application.bkg-context-path}/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  public BookingRefStatusTO createBooking(@Valid @RequestBody BookingTO bookingTO) {
    assert bookingTO.carrierBookingRequestReference() == null;
    return bookingService.createBooking(bookingTO);
  }

  @GetMapping(
      path = "${spring.application.bkg-context-path}/bookings/{carrierBookingRequestReference}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<BookingTO> getBooking(
      @Valid @PathVariable("carrierBookingRequestReference") @NotNull @Size(max = 100)
          String carrierBookingRequestReference) {
    return bookingService
        .getBooking(carrierBookingRequestReference)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
