package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.DocumentPartyRepository;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.edocumentation.service.mapping.DocumentPartyMapper;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentPartyService {

  private final DocumentPartyRepository documentPartyRepository;

  private final DocumentPartyMapper documentPartyMapper;
  private final DisplayedAddressMapper displayedAddressMapper;
  private final PartyService partyService;

  @Transactional(TxType.MANDATORY)
  public void createDocumentParties(Collection<DocumentPartyTO> documentParties, Booking booking) {
    if (documentParties != null && !documentParties.isEmpty()) {
      documentParties.forEach(documentPartyTO -> createDocumentParty(documentPartyTO, booking));
    }
  }

  @Transactional(TxType.MANDATORY)
  public void createDocumentParties(
      Collection<DocumentPartyTO> documentParties, ShippingInstruction shippingInstruction) {
    if (documentParties != null && !documentParties.isEmpty()) {
      documentParties.forEach(
          documentPartyTO ->
              createDocumentPartyAndDisplayedAddress(
                  documentPartyTO.displayedAddress(),
                  documentPartyMapper.toDAO(documentPartyTO).toBuilder()
                      .shippingInstructionID(shippingInstruction.getId())
                      .party(partyService.createParty(documentPartyTO.party()))
                      .build()));
    }
  }

  private void createDocumentParty(DocumentPartyTO documentPartyTO, Booking booking) {
    DocumentParty documentPartyWithBooking =
        documentPartyMapper.toDAO(documentPartyTO, booking).toBuilder()
            .party(partyService.createParty(documentPartyTO.party()))
            .displayedAddress(displayedAddressMapper.toDAO(documentPartyTO.displayedAddress()))
            .build();

    documentPartyRepository.save(documentPartyWithBooking);
  }

  private void createDocumentPartyAndDisplayedAddress(
      List<String> displayedAddress, DocumentParty documentParty) {

    documentPartyRepository.save(
        documentParty.toBuilder()
            .displayedAddress(displayedAddressMapper.toDAO(displayedAddress))
            .build());
  }
}
