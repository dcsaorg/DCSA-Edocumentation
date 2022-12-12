package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.DocumentPartyDataFactory;
import org.dcsa.edocumentation.datafactories.ShippingInstructionDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.*;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.edocumentation.service.mapping.DocumentPartyMapper;
import org.dcsa.edocumentation.service.mapping.PartyMapper;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.skernel.infrastructure.services.AddressService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentPartyServiceTest {
  @Mock private AddressService addressService;

  @Mock private DocumentPartyRepository documentPartyRepository;
  @Mock private PartyRepository partyRepository;
  @Mock private PartyContactDetailsRepository partyContactDetailsRepository;
  @Mock private DisplayedAddressRepository displayedAddressRepository;
  @Mock private PartyIdentifyingCodeRepository partyIdentifyingCodeRepository;
  @Mock private PartyService partyService;

  @Spy
  private DocumentPartyMapper documentPartyMapper = Mappers.getMapper(DocumentPartyMapper.class);

  @Spy
  private PartyMapper partyMapper = Mappers.getMapper(PartyMapper.class);

  @Spy private DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();

  @InjectMocks private DocumentPartyService documentPartyService;

  @BeforeEach
  public void resetMocks() {
    reset(
        addressService,
        documentPartyRepository,
        partyRepository,
        partyContactDetailsRepository,
        displayedAddressRepository,
        partyIdentifyingCodeRepository,
        documentPartyMapper,
        displayedAddressMapper);
  }

  @Test
  void documentPartyServiceTest_testCreateNullWithBooking() {
    documentPartyService.createDocumentParties(null, (Booking) null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  void documentPartyServiceTest_testCreateNullWithShippingInstruction() {
    documentPartyService.createDocumentParties(null, (ShippingInstruction) null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  void documentPartyServiceTest_testCreateEmptyWithBooking() {
    documentPartyService.createDocumentParties(Collections.emptyList(), (Booking) null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  void documentPartyServiceTest_testCreateEmptyWithShippingInstruction() {
    documentPartyService.createDocumentParties(Collections.emptyList(), (ShippingInstruction) null);

    verify(addressService, never()).ensureResolvable(any());
    verify(documentPartyRepository, never()).save(any());
    verify(documentPartyMapper, never()).toDAO(any(DocumentPartyTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyContactDetailsTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyIdentifyingCodeTO.class), any());
    verify(partyMapper, never()).toDAO(any(PartyTO.class));
    verify(addressService, never()).ensureResolvable(any());
    verify(partyContactDetailsRepository, never()).saveAll(any());
    verify(partyIdentifyingCodeRepository, never()).saveAll(any());
  }

  @Test
  void documentPartyServiceTest_createFullDocumentPartyWithBooking() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    DocumentPartyTO documentPartyTO = DocumentPartyDataFactory.fullDocumentPartyTO();
    Party party = DocumentPartyDataFactory.partialParty();
    DocumentParty documentParty = DocumentPartyDataFactory.partialDocumentParty(party);

    when(partyService.createParty(any())).thenReturn(party);
    when(documentPartyRepository.save(any())).thenReturn(documentParty);

    // Execute
    documentPartyService.createDocumentParties(List.of(documentPartyTO), booking);

    // Verify
    verify(partyService).createParty(documentPartyTO.party());
    verify(documentPartyRepository).save(documentParty);
    verify(displayedAddressRepository)
        .saveAll(DocumentPartyDataFactory.displayedAddresses(documentParty));
  }

  @Test
  void documentPartyServiceTest_createFullDocumentPartyWithShippingInstruction() {
    // Setup
    ShippingInstruction shippingInstruction =
        ShippingInstructionDataFactory.singleShallowShippingInstruction();
    DocumentPartyTO documentPartyTO = DocumentPartyDataFactory.fullDocumentPartyTO();
    Party party = DocumentPartyDataFactory.partialParty();
    DocumentParty documentParty =
        DocumentPartyDataFactory.partialDocumentParty(party).toBuilder()
            .shippingInstructionID(shippingInstruction.getId())
            .build();

    when(partyService.createParty(any())).thenReturn(party);
    when(documentPartyRepository.save(any())).thenReturn(documentParty);

    // Execute
    documentPartyService.createDocumentParties(List.of(documentPartyTO), shippingInstruction);

    // Verify
    verify(partyService).createParty(documentPartyTO.party());
    verify(documentPartyRepository).save(documentParty);
    verify(displayedAddressRepository)
        .saveAll(DocumentPartyDataFactory.displayedAddresses(documentParty));
  }
}
