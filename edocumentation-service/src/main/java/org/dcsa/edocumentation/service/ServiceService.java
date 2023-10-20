package org.dcsa.edocumentation.service;

import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Service;
import org.dcsa.edocumentation.domain.persistence.repository.ServiceRepository;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Example;

import jakarta.transaction.Transactional;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceService {
  private final ServiceRepository serviceRepository;

  @Transactional
  public Service resolveService(BookingRequestTO bookingRequest) {
    String carrierServiceCode = bookingRequest.carrierServiceCode();
    String universalServiceReference = bookingRequest.universalServiceReference();

    if (carrierServiceCode == null && universalServiceReference == null) {
      return null;
    }

    Service example = Service.builder()
      .carrierServiceCode(carrierServiceCode)
      .universalServiceReference(universalServiceReference)
      .build();

    return serviceRepository.findAll(Example.of(example)).stream()
      .findFirst()
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No services that match carrierServiceCode '" + carrierServiceCode
          + "' and universalServiceReference '" + universalServiceReference + "'"));
  }
}
