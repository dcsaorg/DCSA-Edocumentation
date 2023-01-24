package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.BookingService;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

/**
 * Emulates SMEs asynchronous validation of a booking.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookingValidationService {
  private final BookingService bookingService;
  private final ShipmentEventRepository shipmentEventRepository;

  private final static Set<BkgDocumentStatus> CAN_BE_VALIDATED =
    Set.of(BkgDocumentStatus.RECE, BkgDocumentStatus.PENC);

  public record ValidationResult(BkgDocumentStatus newStatus, String reason) {}

  public ValidationResult validateBooking(String carrierBookingRequestReference) {
    BookingTO bookingTO = bookingService.getBooking(carrierBookingRequestReference);

    if (!CAN_BE_VALIDATED.contains(bookingTO.documentStatus())) {
      throw ConcreteRequestErrorMessageException.conflict(
        "documentStatus must be one of " + CAN_BE_VALIDATED, null);
    }

    String reason = validateBooking(bookingTO);
    BookingRefStatusTO bookingRefStatusTO = null;
    if (reason == null) {
      reason = "Booking passed validation";
      bookingRefStatusTO = bookingService.updateBookingStatus(carrierBookingRequestReference, BkgDocumentStatus.PENC, reason);
      log.debug("Booking {} passed validation", carrierBookingRequestReference);
    } else {
      bookingRefStatusTO = bookingService.updateBookingStatus(carrierBookingRequestReference, BkgDocumentStatus.PENU, reason);
      log.debug("Booking {} failed validation because {}", carrierBookingRequestReference, reason);
    }
    return new ValidationResult(bookingRefStatusTO.documentStatus(), reason);
  }

  /**
   * Subject to change. Reefer will probably change it.
   */
  private String validateBooking(BookingTO bookingTO) {
    LocalDate today = LocalDate.now();

    if (isInThePast(today, bookingTO.expectedDepartureDate())) {
      return "expectedDepartureDate is in the past";
    }
    if (isInThePast(today, bookingTO.expectedArrivalAtPlaceOfDeliveryStartDate())) {
      return "expectedArrivalAtPlaceOfDeliveryStartDate is in the past";
    }
    if (isInThePast(today, bookingTO.expectedArrivalAtPlaceOfDeliveryEndDate())) {
      return "expectedArrivalAtPlaceOfDeliveryEndDate is in the past";
    }

    return null;
  }

  private boolean isInThePast(LocalDate today, LocalDate time) {
    return time != null && today.isAfter(time);
  }
}
