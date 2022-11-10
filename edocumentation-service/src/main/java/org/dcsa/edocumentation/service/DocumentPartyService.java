package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.DocumentPartyRepository;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DocumentPartyService {
  private final LocationService locationService;
  private final DocumentPartyRepository documentPartyRepository;

  @Transactional(TxType.MANDATORY)
  public void createDocumentParties(Collection<DocumentPartyTO> documentParties, Booking booking) {
    if (documentParties != null && !documentParties.isEmpty()) {
      // documentParties.stream()
      //   .map()
    }
  }
}
