package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/** Emulates SMEs asynchronous validation of a booking. */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingValidationService {
  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;
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
  public ValidationResult<BkgDocumentStatus> validateBooking(Booking booking, boolean persistOnPENC) {
    var carrierBookingRequestReference = booking.getCarrierBookingRequestReference();

    ValidationResult<BkgDocumentStatus> validationResult;
    try {
      validationResult = booking.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict(e.getLocalizedMessage(), e);
    }

    if (validationResult.validationErrors().isEmpty()) {
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
      if (persistOnPENC) {
        var shipmentEvent = booking.pendingConfirmation("Booking passed validation", OffsetDateTime.now());
        bookingRepository.save(booking);
        shipmentEventRepository.save(shipmentEvent);
      }
    } else {
      String reason = validationResult.presentErrors(5000);
      var shipmentEvent = booking.pendingUpdate(reason, OffsetDateTime.now());
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
      bookingRepository.save(booking);
      shipmentEventRepository.save(shipmentEvent);
    }
    return validationResult;
  }

}
