package org.dcsa.edocumentation.service.unofficial;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

/** Emulates SMEs asynchronous validation of a booking. */
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
    return validateShipmentLocations(booking.getShipmentLocations());
  }

  private boolean isInThePast(LocalDate today, LocalDate time) {
    return time != null && today.isAfter(time);
  }

  private String validateShipmentLocations(Set<ShipmentLocation> shipmentLocations) {
    if (shipmentLocations.isEmpty()) {
      return "Invalid booking: Shipment locations should not be empty";
    }

    boolean hasPREorPOL =
        shipmentLocations.stream()
            .map(ShipmentLocation::getShipmentLocationTypeCode)
            .anyMatch(code -> code == LocationType.PRE || code == LocationType.POL);

    if (!hasPREorPOL) {
      return "No ShipmentLocationTypeCode of PRE or POL found in the shipmentLocations, "
          + "either one should be provided both is allowed";
    }

    boolean hasPODorPDE =
        shipmentLocations.stream()
            .map(ShipmentLocation::getShipmentLocationTypeCode)
            .anyMatch(code -> code == LocationType.POD || code == LocationType.PDE);

    if (!hasPODorPDE) {
      return "No ShipmentLocationTypeCode of POD or PDE found in the shipmentLocations, "
          + "either one should be provided both is allowed";
    }

    var filteredByUNLocationCode =
        shipmentLocations.stream()
            .filter(sl -> sl.getLocation().getUNLocationCode() != null)
            .toList();

    var filteredByUNLocationCodeCount = filteredByUNLocationCode.size();

    boolean hasUniqueUNLocationCodes =
        filteredByUNLocationCode.stream()
                .map(sl -> sl.getLocation().getUNLocationCode())
                .distinct()
                .count()
            == filteredByUNLocationCodeCount;

    if (!hasUniqueUNLocationCodes) {
      return "Duplicate UNLocationCodes found in shipmentLocations";
    }

    var filteredByAddress =
            shipmentLocations.stream().filter(sl -> sl.getLocation().getAddress() != null).toList();

    var filteredByAddressCount =  filteredByAddress.size();

    boolean hasUniqueAddresses =
        shipmentLocations.stream()
                .filter(sl -> sl.getLocation().getAddress() != null)
                .map(sl -> sl.getLocation().getAddress())
                .filter(
                    address ->
                        filteredByAddress.stream()
                            .noneMatch(other -> other.getLocation().getAddress().equals(address)))
                .count() == filteredByAddressCount;

    if (!hasUniqueAddresses) {
      return "Duplicate addresses found in shipmentLocations";
    }

    return null;
  }
}
