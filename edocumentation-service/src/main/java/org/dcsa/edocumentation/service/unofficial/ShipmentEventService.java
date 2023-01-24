package org.dcsa.edocumentation.service.unofficial;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.domain.decoupled.repository.specification.ShipmentEventSpecification.ShipmentEventFilters;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import static org.dcsa.edocumentation.domain.decoupled.repository.specification.ShipmentEventSpecification.withFilters;

@Service
@RequiredArgsConstructor
public class ShipmentEventService {
  private final ShipmentEventRepository shipmentEventRepository;

  @Transactional
  public PagedResult<ShipmentEvent> findAll(Pageable pageable, ShipmentEventFilters filters) {
    return new PagedResult<>(shipmentEventRepository.findAll(withFilters(filters), pageable));
  }
}
