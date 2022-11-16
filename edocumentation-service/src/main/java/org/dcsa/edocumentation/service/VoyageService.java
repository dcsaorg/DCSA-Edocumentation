package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VoyageService {
  private final VoyageRepository voyageRepository;

  @Transactional
  public Voyage resolveVoyage(BookingTO bookingRequest) {
    String universalExportVoyageReference = bookingRequest.universalExportVoyageReference();
    String carrierExportVoyageNumber = bookingRequest.carrierExportVoyageNumber();
    if (universalExportVoyageReference != null) {
      // Since universalVoyageReference is not unique in Voyage and booking does not supply a service to make it
      // unique we just take the first Voyage found.
      return voyageRepository.findByUniversalVoyageReference(universalExportVoyageReference).stream()
        .filter(voyage -> carrierExportVoyageNumber == null || carrierExportVoyageNumber.equals(voyage.getCarrierVoyageNumber()))
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No voyages with universalVoyageReference = '" + universalExportVoyageReference
            + "' and carrierExportVoyageNumber = '" + carrierExportVoyageNumber + "'"));
    } else if (carrierExportVoyageNumber != null) {
      // Since carrierVoyageNumber is not unique in Voyage and booking does not supply a service to make it
      // unique we just take the first Voyage found.
      return voyageRepository.findByCarrierVoyageNumber(carrierExportVoyageNumber).stream()
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No voyages with carrierVoyageNumber = '" + carrierExportVoyageNumber + "'"));
    }
    return null;
  }
}
