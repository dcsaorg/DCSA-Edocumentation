package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRequestRepository;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.mapping.BookingRequestMapper;
import org.dcsa.edocumentation.transferobjects.unofficial.BookingCancelRequestTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Emulates SMEs asynchronous validation of a booking. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialBookingRequestService {
  private final BookingRequestRepository bookingRequestRepository;
  private final BookingRequestMapper bookingRequestMapper;
  @Qualifier("eagerValidator")
  private final Validator validator;

  @Transactional
  public BookingRequestRefStatusTO performBookingValidation(String carrierBookingRequestReference) {
    BookingRequest bookingRequest = bookingRequestRepository.findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
            .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
                    "No booking request found with carrierBookingRequestReference='" + carrierBookingRequestReference + "'"));

    validateBooking(bookingRequest, true);

    return bookingRequestMapper.toStatusDTO(bookingRequest);
  }

  @Transactional
  public ValidationResult<String> validateBooking(BookingRequest bookingRequest, boolean persistOnPENC) {
    var carrierBookingRequestReference = bookingRequest.getCarrierBookingRequestReference();

    ValidationResult<String> validationResult;
    try {
      validationResult = bookingRequest.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict(e.getLocalizedMessage(), e);
    }

    if (validationResult.validationErrors().isEmpty()) {
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
      if (persistOnPENC) {
        bookingRequest.pendingUpdatesConfirmation("Booking passed validation", OffsetDateTime.now());
        bookingRequestRepository.save(bookingRequest);
      }
    } else {
      String reason = validationResult.presentErrors(5000);
      bookingRequest.pendingUpdate(reason, OffsetDateTime.now());
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
      bookingRequestRepository.save(bookingRequest);
    }
    return validationResult;
  }

  @Transactional
  public Optional<BookingRequestRefStatusTO> cancelBooking(String carrierBookingRequestReference,
                                                           BookingCancelRequestTO bookingCancelRequestTO) {
    BookingRequest bookingRequest = bookingRequestRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (bookingRequest == null) {
      return Optional.empty();
    }

    /*
    API Caller (typically carrier) can cancel booking by requesting to change booking status to:
    - REJECTED (in the case that booking is still pre-confirmed state)
    - DECLINED (in the case that booking has already been confirmed)
     */
    if (bookingCancelRequestTO.bookingStatus().equals(BookingStatus.REJECTED)) {
      bookingRequest.reject(bookingCancelRequestTO.reason());
    } else if (bookingCancelRequestTO.bookingStatus().equals(BookingStatus.DECLINED)) {
      bookingRequest.decline(bookingCancelRequestTO.reason());
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput("bookingStatus must be either REJECTED or DECLINED");
    }

    bookingRequest = bookingRequestRepository.save(bookingRequest);
    return Optional.of(bookingRequestMapper.toStatusDTO(bookingRequest));
  }
}
