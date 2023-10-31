package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.ConfirmedBookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmedBookingService {
  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;

  public ConfirmedBookingTO findConfirmedBooking(String carrierBookingReference) {
    return bookingRepository
        .findByCarrierBookingReference(carrierBookingReference)
        .map(bookingMapper::toConfirmedDTO)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No Confirmed Booking found with carrierBookingReference = " + carrierBookingReference));
  }
}
