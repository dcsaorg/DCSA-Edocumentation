package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.service.BookingService;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

  @GetMapping(
    path = "${spring.application.bkg-context-path}/bookings/{carrierBookingRequestReference}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public BookingTO getBooking(@PathVariable("carrierBookingRequestReference") @NotBlank @Size(max = 100)
                              String carrierBookingRequestReference) {
    return bookingService
      .getBooking(carrierBookingRequestReference)
      .orElseThrow(
        () ->
          ConcreteRequestErrorMessageException.notFound(
            "No booking found with carrierBookingRequestReference: "
              + carrierBookingRequestReference));
  }

  @PostMapping(path = "${spring.application.bkg-context-path}/bookings")
  @ResponseStatus(HttpStatus.CREATED)
  public BookingRefStatusTO createBooking(@Valid @RequestBody BookingTO bookingRequest) {
    if (bookingRequest.carrierBookingRequestReference() != null
      || bookingRequest.documentStatus() != null
      || bookingRequest.bookingRequestCreatedDateTime() != null
      || bookingRequest.bookingRequestUpdatedDateTime() != null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "carrierBookingRequestReference, documentStatus, bookingRequestCreatedDateTime and"
          + " bookingRequestUpdatedDateTime are not allowed when creating a booking");
    }
    return bookingService.createBooking(bookingRequest);
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
    if (bookingRequest.documentStatus() != null
      || bookingRequest.bookingRequestCreatedDateTime() != null
      || bookingRequest.bookingRequestUpdatedDateTime() != null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "documentStatus, bookingRequestCreatedDateTime and"
          + " bookingRequestUpdatedDateTime are not allowed when updating a booking");
    }
    return bookingService.updateBooking(carrierBookingRequestReference, bookingRequest);
  }
}
