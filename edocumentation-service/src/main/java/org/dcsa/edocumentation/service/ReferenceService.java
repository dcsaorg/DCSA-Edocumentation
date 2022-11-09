package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.ReferenceRepository;
import org.dcsa.edocumentation.service.mapping.ReferenceMapper;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ReferenceService {
  private final ReferenceRepository referenceRepository;
  private final ReferenceMapper referenceMapper;

  public void createReferences(Collection<ReferenceTO> references, Booking booking) {
    if (references != null && !references.isEmpty()) {
      referenceRepository.saveAll(
        references.stream()
          .map(r -> referenceMapper.toDAO(r, booking))
          .toList()
      );
    }
  }
}
