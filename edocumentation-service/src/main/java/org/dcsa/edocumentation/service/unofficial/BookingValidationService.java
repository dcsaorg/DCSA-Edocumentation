package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.unofficial.ValidationResult;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.unofficial.ValidationResultTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

/** Emulates SMEs asynchronous validation of a booking. */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingValidationService {
  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;
  private final DocumentStatusMapper documentStatusMapper;
  private final Validator validator;

  @Transactional
  public ValidationResult<BkgDocumentStatus> validateBooking(String carrierBookingRequestReference) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
            .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
                    "No booking found with carrierBookingRequestReference='" + carrierBookingRequestReference + "'"));

    ValidationResult<BkgDocumentStatus> validationResult = validateBooking(booking);

    if (validationResult.validationErrors().isEmpty()) {
      ShipmentEvent shipmentEvent = booking.pendingConfirmation("Booking passed validation", OffsetDateTime.now());
      bookingRepository.save(booking);
      shipmentEventRepository.save(shipmentEvent);
    }
    return validationResult;
  }

  @Transactional
  public ValidationResult<BkgDocumentStatus> validateBooking(Booking booking) {
    var carrierBookingRequestReference = booking.getCarrierBookingRequestReference();

    ValidationResult<BkgDocumentStatus> validationResult;
    try {
      validationResult = booking.asyncValidation(validator);
    } catch (IllegalStateException e) {
      throw ConcreteRequestErrorMessageException.conflict(e.getLocalizedMessage(), e);
    }
    ShipmentEvent shipmentEvent;

    if (validationResult.validationErrors().isEmpty()) {
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
    } else {
      String reason = validationResult.presentErrors(5000);
      shipmentEvent = booking.pendingUpdate(reason, OffsetDateTime.now());
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
      bookingRepository.save(booking);
      shipmentEventRepository.save(shipmentEvent);
    }
    return validationResult;
  }

  @Transactional
  public ValidationResultTO<org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus> validateBookingResultTO(String carrierBookingRequestReference) {
    var r = validateBooking(carrierBookingRequestReference);
    return new ValidationResultTO<>(
      this.documentStatusMapper.toTransferBkgDocumentStatus(r.proposedStatus()),
      r.presentErrors(5000)
    );
  }

}
