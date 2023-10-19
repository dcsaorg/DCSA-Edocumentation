package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.repository.VesselRepository;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VesselService {
  private final VesselRepository vesselRepository;

  @Transactional
  public Vessel resolveVessel(BookingRequestTO bookingRequest) {
    if (bookingRequest.vesselIMONumber() != null) {
      return vesselRepository.findByVesselIMONumber(bookingRequest.vesselIMONumber())
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No vessel with vesselIMONumber = '" + bookingRequest.vesselIMONumber() + "'"));
    }
    return null;
  }
}
