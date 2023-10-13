package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.ConfirmedBookingRepository;
import org.dcsa.edocumentation.service.mapping.ConfirmedBookingMapper;
import org.dcsa.edocumentation.transferobjects.ConfirmedBookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmedBookingService {
  private final ConfirmedBookingRepository confirmedBookingRepository;
  private final ConfirmedBookingMapper confirmedBookingMapper;

  public ConfirmedBookingTO findConfirmedBooking(String carrierBookingReference) {
    return confirmedBookingRepository
        .findConfirmedBookingByCarrierBookingReference(carrierBookingReference)
        .map(confirmedBookingMapper::confirmedBookingToConfirmedBookingTO)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No Confirmed Booking found with carrierBookingReference = " + carrierBookingReference));
  }
}
