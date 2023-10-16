package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.unofficial.BookingCancelRequestTO;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Emulates SMEs asynchronous validation of a booking. */
@Slf4j
@Service
@RequiredArgsConstructor
public class UnofficialBookingService {
  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;
  @Qualifier("eagerValidator")
  private final Validator validator;

  @Transactional
  public BookingRefStatusTO performBookingValidation(String carrierBookingRequestReference) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
            .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
                    "No booking found with carrierBookingRequestReference='" + carrierBookingRequestReference + "'"));

    validateBooking(booking, true);

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public ValidationResult<String> validateBooking(Booking booking, boolean persistOnPENC) {
    var carrierBookingRequestReference = booking.getCarrierBookingRequestReference();

    ValidationResult<String> validationResult;
    try {
      validationResult = booking.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict(e.getLocalizedMessage(), e);
    }

    if (validationResult.validationErrors().isEmpty()) {
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
      if (persistOnPENC) {
        booking.pendingUpdatesConfirmation("Booking passed validation", OffsetDateTime.now());
        bookingRepository.save(booking);
      }
    } else {
      String reason = validationResult.presentErrors(5000);
      booking.pendingUpdate(reason, OffsetDateTime.now());
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
      bookingRepository.save(booking);
    }
    return validationResult;
  }

  @Transactional
  public Optional<BookingRefStatusTO> cancelBooking(String carrierBookingRequestReference,
                                                    BookingCancelRequestTO bookingCancelRequestTO) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(
      carrierBookingRequestReference
    ).orElse(null);
    if (booking == null) {
      return Optional.empty();
    }

    /*
    API Caller (typically carrier) can cancel booking by requesting to change booking status to:
    - REJECTED (in the case that booking is still pre-confirmed state)
    - DECLINED (in the case that booking has already been confirmed)
     */
    if (bookingCancelRequestTO.bookingStatus().equals(BookingStatus.REJECTED)) {
      booking.reject(bookingCancelRequestTO.reason());
    } else if (bookingCancelRequestTO.bookingStatus().equals(BookingStatus.DECLINED)) {
      booking.decline(bookingCancelRequestTO.reason());
    } else {
      throw ConcreteRequestErrorMessageException.invalidInput("bookingStatus must be either REJECTED or DECLINED");
    }

    booking = bookingRepository.save(booking);
    return Optional.of(bookingMapper.toStatusDTO(booking));
  }
}
