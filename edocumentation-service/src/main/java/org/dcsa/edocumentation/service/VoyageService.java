package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoyageService {
  private final VoyageRepository voyageRepository;

  public Voyage resolveVoyage(BookingTO bookingRequest) {
    if (bookingRequest.carrierExportVoyageNumber() != null) {
      // Since carrierVoyageNumber is not unique in Voyage and booking does not supply a service to make it
      // unique we just take the first Voyage found.
      return voyageRepository.findByCarrierVoyageNumber(bookingRequest.carrierExportVoyageNumber()).stream()
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No voyages with carrierVoyageNumber = '" + bookingRequest.carrierExportVoyageNumber() + "'"));
    }
    return null;
  }
}
