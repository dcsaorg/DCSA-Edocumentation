package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.DocumentPartyDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DocumentPartyServiceTest {
  @Mock private AddressService addressService;

  @Mock private DocumentPartyRepository documentPartyRepository;
  @Mock private PartyRepository partyRepository;
  @Mock private PartyContactDetailsRepository partyContactDetailsRepository;
  @Mock private DisplayedAddressRepository displayedAddressRepository;
  @Mock private PartyIdentifyingCodeRepository partyIdentifyingCodeRepository;

  @Spy private DocumentPartyMapper documentPartyMapper = Mappers.getMapper(DocumentPartyMapper.class);
  @Spy private DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();

  @InjectMocks private DocumentPartyService documentPartyService;

  @BeforeEach
  public void resetMocks() {
    reset(addressService, documentPartyRepository, partyRepository, partyContactDetailsRepository,
      displayedAddressRepository, partyIdentifyingCodeRepository, documentPartyMapper, displayedAddressMapper);
  }

  @Test
  public void testCreateNull() {
    documentPartyService.createDocumentParties(null, null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  public void testCreateEmpty() {
    documentPartyService.createDocumentParties(Collections.emptyList(), null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(documentPartyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  public void createFullDocumentParty() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    DocumentPartyTO documentPartyTO = DocumentPartyDataFactory.fullDocumentPartyTO();
    Party party = DocumentPartyDataFactory.partialParty();
    DocumentParty documentParty = DocumentPartyDataFactory.partialDocumentParty(party);

    when(addressService.ensureResolvable(any())).thenReturn(party.getAddress());
    when(partyRepository.save(any())).thenReturn(party);
    when(documentPartyRepository.save(any())).thenReturn(documentParty);

    // Execute
    documentPartyService.createDocumentParties(List.of(documentPartyTO), booking);

    // Verify
    verify(addressService).ensureResolvable(documentPartyTO.party().address());
    verify(partyRepository).save(party);
    verify(partyContactDetailsRepository).saveAll(List.of(DocumentPartyDataFactory.partyContactDetails(party)));
    verify(partyIdentifyingCodeRepository).saveAll(List.of(DocumentPartyDataFactory.partyIdentifyingCode(party)));
    verify(documentPartyRepository).save(documentParty);
    verify(displayedAddressRepository).saveAll(DocumentPartyDataFactory.displayedAddresses(documentParty));
  }
}