package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.ValueAddedServiceRequestRepository;
import org.dcsa.edocumentation.service.mapping.ValueAddedServiceRequestMapper;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceRequestTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ValueAddedServiceRequestService {
  private final ValueAddedServiceRequestRepository valueAddedServiceRequestRepository;
  private final ValueAddedServiceRequestMapper valueAddedServiceRequestMapper;

  public void createValueAddedServiceRequests(Collection<ValueAddedServiceRequestTO> valueAddedServiceRequests, Booking booking) {
    if (valueAddedServiceRequests != null && !valueAddedServiceRequests.isEmpty()) {
      valueAddedServiceRequestRepository.saveAll(
        valueAddedServiceRequests.stream()
          .map(to -> valueAddedServiceRequestMapper.toDAO(to, booking))
          .toList()
      );
    }
  }
}
