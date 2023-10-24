package org.dcsa.edocumentation.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.datafactories.VesselDataFactory;
import org.dcsa.edocumentation.datafactories.VoyageDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRequestRepository;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.service.mapping.*;
import org.dcsa.edocumentation.transferobjects.BookingRequestRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class BookingRequestServiceTest {
  @Nested
  class GetBookingRequest {
    @Mock private BookingRequestRepository repository;

    @Spy private BookingRequestMapper bookingRequestMapper = Mappers.getMapper(BookingRequestMapper.class);
    @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Spy private LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Spy private ShipmentLocationMapper shipmentLocationMapper = Mappers.getMapper(ShipmentLocationMapper.class);
    @Spy private PartyMapper partyMapper = Mappers.getMapper(PartyMapper.class);

    @Spy private DocumentPartyMapper documentPartyMapper = Mappers.getMapper(DocumentPartyMapper.class);
    @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
    @Spy private ActiveReeferSettingsMapper activeReeferSettingsMapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);

    @Spy private TaxAndLegalReferenceMapper taxAndLegalReferenceMapper = Mappers.getMapper(TaxAndLegalReferenceMapper.class);
    @InjectMocks private BookingRequestService service;

    @BeforeEach
    void setupMappers() {
      DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();
      ReflectionTestUtils.setField(bookingRequestMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "documentPartyMapper", documentPartyMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "shipmentLocationMapper", shipmentLocationMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "requestedEquipmentGroupMapper", requestedEquipmentGroupMapper);
      ReflectionTestUtils.setField(requestedEquipmentGroupMapper, "activeReeferSettingsMapper", activeReeferSettingsMapper);
      ReflectionTestUtils.setField(documentPartyMapper, "displayedAddressMapper", displayedAddressMapper);
      ReflectionTestUtils.setField(documentPartyMapper, "partyMapper", partyMapper);
      ReflectionTestUtils.setField(partyMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(locationMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(shipmentLocationMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(partyMapper, "taxAndLegalReferenceMapper", taxAndLegalReferenceMapper);
    }

    @Test
    void bookingServiceTest_testGetFullBooking() {
      when(repository.findBookingByCarrierBookingRequestReference(any()))
        .thenReturn(Optional.of(BookingDataFactory.singleDeepBooking()));

      Optional<BookingRequestTO> result = service.getBookingRequest("test");
      assertTrue(result.isPresent());

      BookingRequestTO bookingResult = result.get();
      assertEquals("BOOKING_REQ_REF_01", bookingResult.carrierBookingRequestReference());
      assertFalse(bookingResult.requestedEquipments().isEmpty());
      assertFalse(bookingResult.references().isEmpty());
      assertFalse(bookingResult.shipmentLocations().isEmpty());
    }

    @Test
    void bookingServiceTest_testGetMinimalBooking() {
      when(repository.findBookingByCarrierBookingRequestReference(any()))
          .thenReturn(Optional.of(BookingDataFactory.singleMinimalBooking()));

      Optional<BookingRequestTO> result = service.getBookingRequest("test");
      assertTrue(result.isPresent());

      BookingRequestTO bookingResult = result.get();
      assertEquals("BOOKING_REQ_REF_01", bookingResult.carrierBookingRequestReference());
      assertFalse(bookingResult.requestedEquipments().isEmpty());
    }

    @Test
    void bookingServiceTest_testNoBookingFound() {
      when(repository.findBookingByCarrierBookingRequestReference(any())).thenReturn(Optional.empty());

      Optional<BookingRequestTO> result = service.getBookingRequest("test");
      assertFalse(result.isPresent());
    }

    @Test
    void bookingServiceTest_testNullCarrierBookingRequestReference() {
      when(repository.findBookingByCarrierBookingRequestReference(null)).thenReturn(Optional.empty());

      Optional<BookingRequestTO> result = service.getBookingRequest(null);
      assertFalse(result.isPresent());
    }
  }

  @Nested
  class CreateBookingRequest {
    @Mock private VoyageService voyageService;
    @Mock private VesselService vesselService;
    @Mock private ReferenceService referenceService;
    @Mock private DocumentPartyService documentPartyService;
    @Mock private ShipmentLocationService shipmentLocationService;

    @Mock private BookingRequestRepository bookingRequestRepository;

    @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Spy private LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);
    @Spy private BookingRequestMapper bookingRequestMapper = Mappers.getMapper(BookingRequestMapper.class);
    @Spy private ReferenceMapper referenceMapper = Mappers.getMapper(ReferenceMapper.class);
    @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
    @Spy private CommodityMapper commodityMapper = Mappers.getMapper(CommodityMapper.class);
    @Spy private ActiveReeferSettingsMapper activeReeferSettingsMapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);

    @Spy private OuterPackagingMapper outerPackagingMapper = Mappers.getMapper(OuterPackagingMapper.class);

    @InjectMocks private BookingRequestService bookingRequestService;

    @BeforeEach
    public void resetMocks() {
      ReflectionTestUtils.setField(locationMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "referenceMapper", referenceMapper);
      ReflectionTestUtils.setField(bookingRequestMapper, "requestedEquipmentGroupMapper", requestedEquipmentGroupMapper);
      ReflectionTestUtils.setField(requestedEquipmentGroupMapper, "activeReeferSettingsMapper", activeReeferSettingsMapper);
      ReflectionTestUtils.setField(requestedEquipmentGroupMapper, "commodityMapper", commodityMapper);
      ReflectionTestUtils.setField(commodityMapper, "outerPackagingMapper", outerPackagingMapper);
      reset(voyageService, vesselService,referenceService, documentPartyService, shipmentLocationService,
        bookingRequestRepository);
    }

    @Test
    void testCreateFullBooking() {
      Voyage voyage = VoyageDataFactory.voyage();
      Vessel vessel = VesselDataFactory.vessel();
      var location = LocationDataFactory.addressLocationWithId();
      OffsetDateTime now = OffsetDateTime.now();

      BookingRequestTO bookingRequest = BookingDataFactory.singleFullBookingRequestTO();
      BookingRequest bookingRequestToSave = bookingRequestMapper.toDAO(bookingRequest, voyage, vessel).toBuilder()
        .placeOfIssue(location)
        .invoicePayableAt(location)
        .build();
      BookingRequest bookingRequestSaved = bookingRequestToSave.toBuilder()
        .carrierBookingRequestReference("carrierBookingRequestRef")
        .bookingRequestCreatedDateTime(now)
        .bookingRequestUpdatedDateTime(now)
        .bookingStatus(BookingStatus.RECEIVED)
        .build();

      when(voyageService.resolveVoyage(any())).thenReturn(voyage);
      when(vesselService.resolveVessel(any())).thenReturn(vessel);
      when(bookingRequestRepository.save(any())).thenReturn(bookingRequestSaved);

       // Execute
      BookingRequestRefStatusTO result = bookingRequestService.createBookingRequest(bookingRequest);

      // Verify
      assertEquals("carrierBookingRequestRef", result.carrierBookingRequestReference());
      assertEquals(now, result.bookingRequestCreatedDateTime());
      assertEquals(now, result.bookingRequestUpdatedDateTime());
      assertEquals(BookingStatus.RECEIVED, result.bookingStatus());

      ArgumentCaptor<BookingRequest> bookingArgumentCaptor = ArgumentCaptor.forClass(BookingRequest.class);
      verify(voyageService).resolveVoyage(bookingRequest);
      verify(vesselService).resolveVessel(bookingRequest);
      verify(bookingRequestRepository).save(bookingArgumentCaptor.capture());
      verify(referenceService).createReferences(eq(bookingRequest.references()), any(BookingRequest.class));
      verify(documentPartyService).createDocumentParties(eq(bookingRequest.documentParties()), any(BookingRequest.class));
      verify(shipmentLocationService).createShipmentLocations(eq(bookingRequest.shipmentLocations()), any(BookingRequest.class));

      BookingRequest bookingRequestActuallySaved = bookingArgumentCaptor.getValue();
      assertEquals(bookingRequestToSave, bookingRequestActuallySaved.toBuilder()
        .id(null)
        .carrierBookingRequestReference(null)
        .bookingRequestCreatedDateTime(null)
        .bookingRequestUpdatedDateTime(null)
        .bookingStatus(null)
        .build());
      assertNotNull(bookingRequestActuallySaved.getCarrierBookingRequestReference());
      assertNotNull(bookingRequestActuallySaved.getBookingRequestCreatedDateTime());
      assertNotNull(bookingRequestActuallySaved.getBookingRequestUpdatedDateTime());
      assertEquals(BookingStatus.RECEIVED, bookingRequestActuallySaved.getBookingStatus());
    }
  }
}
