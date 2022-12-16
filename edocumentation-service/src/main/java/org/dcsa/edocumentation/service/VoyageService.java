package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class VoyageService {
  private final ServiceService serviceService;
  private final VoyageRepository voyageRepository;

  @Transactional
  public Voyage resolveVoyage(BookingTO bookingRequest) {
    String universalExportVoyageReference = bookingRequest.universalExportVoyageReference();
    String carrierExportVoyageNumber = bookingRequest.carrierExportVoyageNumber();

    if (universalExportVoyageReference == null && carrierExportVoyageNumber == null) {
      if (bookingRequest.carrierServiceCode() != null || bookingRequest.universalServiceReference() != null) {
        throw ConcreteRequestErrorMessageException.invalidInput(
          "carrierServiceCode and/or universalServiceReference provided but both universalExportVoyageReference and carrierExportVoyageNumber are missing");
      }
      return null;
    }

    Voyage example = Voyage.builder()
      .universalVoyageReference(universalExportVoyageReference)
      .carrierVoyageNumber(carrierExportVoyageNumber)
      .service(serviceService.resolveService(bookingRequest))
      .build();

      return voyageRepository.findAll(Example.of(example)).stream()
        .findFirst()
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No voyages with universalVoyageReference = '" + universalExportVoyageReference
            + "' and carrierExportVoyageNumber = '" + carrierExportVoyageNumber + "'"));
  }
}
