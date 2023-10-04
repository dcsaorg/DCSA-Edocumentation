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
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.*;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
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
class BookingServiceTest {
  @Nested
  class GetBooking {
    @Mock private BookingRepository repository;

    @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Spy private LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

    @Spy private ShipmentLocationMapper shipmentLocationMapper = Mappers.getMapper(ShipmentLocationMapper.class);
    @Spy private PartyMapper partyMapper = Mappers.getMapper(PartyMapper.class);

    @Spy private DocumentPartyMapper documentPartyMapper = Mappers.getMapper(DocumentPartyMapper.class);
    @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
    @Spy private ActiveReeferSettingsMapper activeReeferSettingsMapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);

    @InjectMocks private BookingService service;

    @BeforeEach
    void setupMappers() {
      DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();
      ReflectionTestUtils.setField(bookingMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(bookingMapper, "documentPartyMapper", documentPartyMapper);
      ReflectionTestUtils.setField(bookingMapper, "shipmentLocationMapper", shipmentLocationMapper);
      ReflectionTestUtils.setField(bookingMapper, "requestedEquipmentGroupMapper", requestedEquipmentGroupMapper);
      ReflectionTestUtils.setField(requestedEquipmentGroupMapper, "activeReeferSettingsMapper", activeReeferSettingsMapper);
      ReflectionTestUtils.setField(documentPartyMapper, "displayedAddressMapper", displayedAddressMapper);
      ReflectionTestUtils.setField(documentPartyMapper, "partyMapper", partyMapper);
      ReflectionTestUtils.setField(partyMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(locationMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(shipmentLocationMapper, "locationMapper", locationMapper);
    }

    @Test
    void bookingServiceTest_testGetFullBooking() {
      when(repository.findBookingByCarrierBookingRequestReference(any()))
        .thenReturn(Optional.of(BookingDataFactory.singleDeepBooking()));

      Optional<BookingTO> result = service.getBooking("test");
      assertTrue(result.isPresent());

      BookingTO bookingResult = result.get();
      assertEquals("BOOKING_REQ_REF_01", bookingResult.carrierBookingRequestReference());
      assertFalse(bookingResult.commodities().isEmpty());
      assertFalse(bookingResult.references().isEmpty());
      assertFalse(bookingResult.shipmentLocations().isEmpty());
    }

    @Test
    void bookingServiceTest_testGetMinimalBooking() {
      when(repository.findBookingByCarrierBookingRequestReference(any()))
          .thenReturn(Optional.of(BookingDataFactory.singleMinimalBooking()));

      Optional<BookingTO> result = service.getBooking("test");
      assertTrue(result.isPresent());

      BookingTO bookingResult = result.get();
      assertEquals("BOOKING_REQ_REF_01", bookingResult.carrierBookingRequestReference());
      assertFalse(bookingResult.commodities().isEmpty());
    }

    @Test
    void bookingServiceTest_testNoBookingFound() {
      when(repository.findBookingByCarrierBookingRequestReference(any())).thenReturn(Optional.empty());

      Optional<BookingTO> result = service.getBooking("test");
      assertFalse(result.isPresent());
    }

    @Test
    void bookingServiceTest_testNullCarrierBookingRequestReference() {
      when(repository.findBookingByCarrierBookingRequestReference(null)).thenReturn(Optional.empty());

      Optional<BookingTO> result = service.getBooking(null);
      assertFalse(result.isPresent());
    }
  }

  @Nested
  class CreateBooking {
    @Mock private VoyageService voyageService;
    @Mock private VesselService vesselService;
    @Mock private CommodityService commodityService;
    @Mock private RequestedEquipmentGroupService requestedEquipmentGroupService;
    @Mock private ReferenceService referenceService;
    @Mock private DocumentPartyService documentPartyService;
    @Mock private ShipmentLocationService shipmentLocationService;

    @Mock private BookingRepository bookingRepository;

    @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Spy private LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);
    @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy private ReferenceMapper referenceMapper = Mappers.getMapper(ReferenceMapper.class);

    @InjectMocks private BookingService bookingService;

    @BeforeEach
    public void resetMocks() {
      ReflectionTestUtils.setField(locationMapper, "addressMapper", addressMapper);
      ReflectionTestUtils.setField(bookingMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(bookingMapper, "referenceMapper", referenceMapper);
      reset(voyageService, vesselService, commodityService,
        requestedEquipmentGroupService, referenceService, documentPartyService, shipmentLocationService,
        bookingRepository);
    }

    @Test
    void testCreateFullBooking() {
      Voyage voyage = VoyageDataFactory.voyage();
      Vessel vessel = VesselDataFactory.vessel();
      var location = LocationDataFactory.addressLocationWithId();
      OffsetDateTime now = OffsetDateTime.now();

      BookingTO bookingRequest = BookingDataFactory.singleFullBookingRequestTO();
      Booking bookingToSave = bookingMapper.toDAO(bookingRequest).toBuilder()
        .vessel(vessel)
        .voyage(voyage)
        .placeOfIssue(location)
        .invoicePayableAt(location)
        .preCarriageUnderShippersResponsibility(DCSATransportType.VESSEL.name())
        .build();
      Booking bookingSaved = bookingToSave.toBuilder()
        .carrierBookingRequestReference("carrierBookingRequestRef")
        .bookingRequestCreatedDateTime(now)
        .bookingRequestUpdatedDateTime(now)
        .bookingStatus(BookingStatus.RECEIVED)
        .build();

      when(voyageService.resolveVoyage(any())).thenReturn(voyage);
      when(vesselService.resolveVessel(any())).thenReturn(vessel);
      when(bookingRepository.save(any())).thenReturn(bookingSaved);

       // Execute
      BookingRefStatusTO result = bookingService.createBooking(bookingRequest);

      // Verify
      assertEquals("carrierBookingRequestRef", result.carrierBookingRequestReference());
      assertEquals(now, result.bookingRequestCreatedDateTime());
      assertEquals(now, result.bookingRequestUpdatedDateTime());
      assertEquals(BookingStatus.RECEIVED, result.bookingStatus());

      ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
      verify(voyageService).resolveVoyage(bookingRequest);
      verify(vesselService).resolveVessel(bookingRequest);
      verify(bookingRepository).save(bookingArgumentCaptor.capture());
      verify(commodityService).createCommodities(eq(bookingRequest.commodities()), any(Booking.class));
      verify(referenceService).createReferences(eq(bookingRequest.references()), any(Booking.class));
      verify(documentPartyService).createDocumentParties(eq(bookingRequest.documentParties()), any(Booking.class));
      verify(shipmentLocationService).createShipmentLocations(eq(bookingRequest.shipmentLocations()), any(Booking.class));

      Booking bookingActuallySaved = bookingArgumentCaptor.getValue();
      assertEquals(bookingToSave, bookingActuallySaved.toBuilder()
        .id(null)
        .carrierBookingRequestReference(null)
        .bookingRequestCreatedDateTime(null)
        .bookingRequestUpdatedDateTime(null)
        .bookingStatus(null)
        .build());
      assertNotNull(bookingActuallySaved.getCarrierBookingRequestReference());
      assertNotNull(bookingActuallySaved.getBookingRequestCreatedDateTime());
      assertNotNull(bookingActuallySaved.getBookingRequestUpdatedDateTime());
      assertEquals(BookingStatus.RECEIVED, bookingActuallySaved.getBookingStatus());
    }
  }
}
