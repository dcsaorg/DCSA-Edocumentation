package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * Emulates SMEs asynchronous validation of a booking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingValidationService {
  private final BookingRepository bookingRepository;
  private final ShipmentEventRepository shipmentEventRepository;

  private final static Set<BkgDocumentStatus> CAN_BE_VALIDATED =
    Set.of(BkgDocumentStatus.RECE, BkgDocumentStatus.PENC);

  public record ValidationResult(BkgDocumentStatus newStatus, String reason) {}

  @Transactional
  public ValidationResult validateBooking(String carrierBookingRequestReference) {
    Booking booking = bookingRepository.findBookingByCarrierBookingRequestReference(carrierBookingRequestReference)
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No booking found with carrierBookingRequestReference='" + carrierBookingRequestReference + "'"));

    if (!CAN_BE_VALIDATED.contains(booking.getDocumentStatus())) {
      throw ConcreteRequestErrorMessageException.conflict(
        "documentStatus must be one of " + CAN_BE_VALIDATED, null);
    }

    ShipmentEvent shipmentEvent;
    String reason = validateBooking(booking);
    if (reason == null) {
      reason = "Booking passed validation";
      shipmentEvent = booking.pendingConfirmation(reason, OffsetDateTime.now());
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
    } else {
      shipmentEvent = booking.pendingUpdate(reason, OffsetDateTime.now());
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
    }
    bookingRepository.save(booking);
    shipmentEventRepository.save(shipmentEvent);
    return new ValidationResult(booking.getDocumentStatus(), reason);
  }

  /**
   * Subject to change. Reefer will probably change it.
   */
  private String validateBooking(Booking booking) {
    LocalDate today = LocalDate.now();

    if (isInThePast(today, booking.getExpectedDepartureDate())) {
      return "expectedDepartureDate is in the past";
    }
    if (isInThePast(today, booking.getExpectedArrivalAtPlaceOfDeliveryStartDate())) {
      return "expectedArrivalAtPlaceOfDeliveryStartDate is in the past";
    }
    if (isInThePast(today, booking.getExpectedArrivalAtPlaceOfDeliveryEndDate())) {
      return "expectedArrivalAtPlaceOfDeliveryEndDate is in the past";
    }

    return null;
  }

  private boolean isInThePast(LocalDate today, LocalDate time) {
    return time != null && today.isAfter(time);
  }
}
