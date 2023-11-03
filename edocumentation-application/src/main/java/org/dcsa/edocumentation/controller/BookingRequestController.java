package org.dcsa.edocumentation.controller;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.BookingRequestService;
import org.dcsa.edocumentation.transferobjects.BookingCancelRequestTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Validated
@RestController
@RequiredArgsConstructor
public class BookingRequestController {
  private final BookingRequestService bookingRequestService;

  @GetMapping(
    path = "${spring.application.bkg-context-path}/booking-requests/{carrierBookingRequestReference}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  public BookingRequestTO getBookingRequest(@PathVariable("carrierBookingRequestReference") @NotBlank @Size(max = 100)
                              String carrierBookingRequestReference) {
    return bookingRequestService
      .getBookingRequest(carrierBookingRequestReference)
      .orElseThrow(
        () ->
          ConcreteRequestErrorMessageException.notFound(
            "No booking request found with carrierBookingRequestReference: "
              + carrierBookingRequestReference));
  }

  @PostMapping(path = "${spring.application.bkg-context-path}/booking-requests")
  @ResponseStatus(HttpStatus.CREATED)
  public BookingRequestRefStatusTO createBookingRequest(@Valid @RequestBody BookingRequestTO bookingRequest) {
    if (bookingRequest.carrierBookingRequestReference() != null
      || bookingRequest.bookingStatus() != null
      || bookingRequest.bookingRequestCreatedDateTime() != null
      || bookingRequest.bookingRequestUpdatedDateTime() != null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "carrierBookingRequestReference, bookingStatus, bookingRequestCreatedDateTime and"
          + " bookingRequestUpdatedDateTime are not allowed when creating a booking request");
    }
    return bookingRequestService.createBookingRequest(bookingRequest);
  }

  @PutMapping(path = "${spring.application.bkg-context-path}/booking-requests/{carrierBookingRequestReference}")
  @ResponseStatus(HttpStatus.OK)
  public BookingRequestRefStatusTO updateBookingRequest(
    @PathVariable("carrierBookingRequestReference")
    @NotBlank @Size(max = 100)
    String carrierBookingRequestReference,

    @Valid @RequestBody
    BookingRequestTO bookingRequest
  ) {
    if (bookingRequest.carrierBookingRequestReference() == null || !carrierBookingRequestReference.equals(bookingRequest.carrierBookingRequestReference())) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "carrierBookingRequestReference must match bookingRequest.carrierBookingRequestReference");
    }
    if (bookingRequest.bookingStatus() != null
      || bookingRequest.bookingRequestCreatedDateTime() != null
      || bookingRequest.bookingRequestUpdatedDateTime() != null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
        "bookingStatus, bookingRequestCreatedDateTime and"
          + " bookingRequestUpdatedDateTime are not allowed when updating a booking request");
    }
    return bookingRequestService.updateBookingRequest(carrierBookingRequestReference, bookingRequest)
      .orElseThrow(
        () ->
          ConcreteRequestErrorMessageException.notFound(
            "No booking request found with carrierBookingRequestReference: "
              + carrierBookingRequestReference));
  }

  @PatchMapping(
    path = "${spring.application.bkg-context-path}/booking-requests/{carrierBookingRequestReference}",
    produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public BookingRequestRefStatusTO cancelBookingRequest(
      @Valid @PathVariable("carrierBookingRequestReference") @NotNull @Size(max = 100)
          String carrierBookingRequestReference,
      @Valid @RequestBody BookingCancelRequestTO bookingCancelRequestTO) {
    if (!bookingCancelRequestTO.bookingStatus().equals(BookingStatus.CANCELLED)) {
      throw ConcreteRequestErrorMessageException.invalidInput("bookingStatus must be CANCELLED");
    }
    return bookingRequestService.cancelBooking(carrierBookingRequestReference, bookingCancelRequestTO.reason())
      .orElseThrow(
        () ->
          ConcreteRequestErrorMessageException.notFound(
            "No booking request found with carrierBookingRequestReference: "
              + carrierBookingRequestReference));
  }
}
