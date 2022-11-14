package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.DisplayedAddressRepository;
import org.dcsa.edocumentation.domain.persistence.repository.DocumentPartyRepository;
import org.dcsa.edocumentation.domain.persistence.repository.PartyContactDetailsRepository;
import org.dcsa.edocumentation.domain.persistence.repository.PartyIdentifyingCodeRepository;
import org.dcsa.edocumentation.domain.persistence.repository.PartyRepository;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.edocumentation.service.mapping.DocumentPartyMapper;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentPartyService {
  private final AddressService addressService;

  private final DocumentPartyRepository documentPartyRepository;
  private final PartyRepository partyRepository;
  private final PartyContactDetailsRepository partyContactDetailsRepository;
  private final DisplayedAddressRepository displayedAddressRepository;
  private final PartyIdentifyingCodeRepository partyIdentifyingCodeRepository;

  private final DocumentPartyMapper documentPartyMapper;
  private final DisplayedAddressMapper displayedAddressMapper;

  @Transactional(TxType.MANDATORY)
  public void createDocumentParties(Collection<DocumentPartyTO> documentParties, Booking booking) {
    if (documentParties != null && !documentParties.isEmpty()) {
      documentParties.forEach(documentPartyTO ->
        createDocumentParty(documentPartyTO,
          documentPartyMapper.toDAO(documentPartyTO).toBuilder().booking(booking)));
    }
  }

  @Transactional(TxType.MANDATORY)
  public void createDocumentParties(Collection<DocumentPartyTO> documentParties, ShippingInstruction shippingInstruction) {
    if (documentParties != null && !documentParties.isEmpty()) {
      documentParties.forEach(documentPartyTO ->
        createDocumentParty(documentPartyTO,
          documentPartyMapper.toDAO(documentPartyTO).toBuilder().shippingInstruction(shippingInstruction)));
    }
  }

  private void createDocumentParty(DocumentPartyTO documentPartyTO, DocumentParty.DocumentPartyBuilder builder) {
    DocumentParty documentParty = documentPartyRepository.save(
      builder
        .party(createParty(documentPartyTO.party()))
        .build());

    createDisplayedAddresses(documentPartyTO, documentParty);
  }

  private void createDisplayedAddresses(DocumentPartyTO documentPartyTO, DocumentParty documentParty) {
    List<String> displayedAddress = documentPartyTO.displayedAddress();
    if (displayedAddress != null && !displayedAddress.isEmpty()) {
      displayedAddressRepository.saveAll(
        displayedAddressMapper.toDAO(displayedAddress, documentParty));
    }
  }

  private Party createParty(PartyTO partyTO) {
    Party party = partyRepository.save(
        documentPartyMapper.toDAO(partyTO).toBuilder()
        .address(addressService.ensureResolvable(partyTO.address()))
        .build());

    List<PartyContactDetailsTO> partyContactDetails = partyTO.partyContactDetails();
    if (partyContactDetails != null && !partyContactDetails.isEmpty()) {
      partyContactDetailsRepository.saveAll(
        partyContactDetails.stream()
          .map(partyContactDetailsTO -> documentPartyMapper.toDAO(partyContactDetailsTO, party))
          .toList());
    }

    List<PartyIdentifyingCodeTO> identifyingCodes = partyTO.identifyingCodes();
    if (identifyingCodes != null && !identifyingCodes.isEmpty()) {
      partyIdentifyingCodeRepository.saveAll(
        identifyingCodes.stream()
          .map(partyIdentifyingCodeTO -> documentPartyMapper.toDAO(partyIdentifyingCodeTO, party))
          .toList()
      );
    }

    return party;
  }
}
