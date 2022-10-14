package org.dcsa.edocumentation.service;

import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.ServiceRepository;
import org.dcsa.edocumentation.service.mapping.ServiceMapper;
import org.dcsa.edocumentation.transferobjects.ServiceTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceService {
  private final ServiceRepository serviceRepository;
  private final ServiceMapper serviceMapper;

  @Transactional
  public List<ServiceTO> findAll() {
    return serviceRepository.findAll().stream()
      .map(serviceMapper::toDTO)
      .toList();
  }
}
