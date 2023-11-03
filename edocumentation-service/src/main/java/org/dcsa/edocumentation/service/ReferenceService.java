package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ReferenceRepository;
import org.dcsa.edocumentation.service.mapping.ReferenceMapper;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReferenceService {
  private final ReferenceRepository referenceRepository;
  private final ReferenceMapper referenceMapper;

  @Transactional(TxType.MANDATORY)
  public void createReferences(Collection<ReferenceTO> references, BookingData bookingData) {
    if (references != null && !references.isEmpty()) {
      List<Reference> referenceDAOs =
          references.stream().map(r -> referenceMapper.toDAO(r, bookingData)).toList();
      saveReferenceDAOs(referenceDAOs);
    }
  }

  @Transactional(TxType.MANDATORY)
  public void createReferences(
      Collection<ReferenceTO> references, ShippingInstruction shippingInstruction) {
    if (references != null && !references.isEmpty()) {
      List<Reference> referenceDAOs =
          references.stream()
              .map(referenceTO -> referenceMapper.toDAO(referenceTO, shippingInstruction))
              .toList();
      saveReferenceDAOs(referenceDAOs);
    }
  }

  private void saveReferenceDAOs(List<Reference> referenceDAOs) {
    referenceRepository.saveAll(referenceDAOs);
  }
}
