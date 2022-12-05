package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.datafactories.VesselDataFactory;
import org.dcsa.edocumentation.datafactories.VoyageDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ShipmentEventTypeCode;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentEventRepository;
import org.dcsa.edocumentation.service.mapping.ActiveReeferSettingsMapper;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.CommodityRequestedEquipmentLinkMapper;
import org.dcsa.edocumentation.service.mapping.DisplayedAddressMapper;
import org.dcsa.edocumentation.service.mapping.ModeOfTransportMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.dcsa.skernel.infrastructure.services.mapping.AddressMapper;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
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

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
  @Nested
  class GetBooking {
    @Mock private BookingRepository repository;

    @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
    @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
    @Spy private CommodityRequestedEquipmentLinkMapper commodityRequestedEquipmentLinkMapper = new CommodityRequestedEquipmentLinkMapper();
    @Spy private ActiveReeferSettingsMapper activeReeferSettingsMapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);

    @InjectMocks private BookingService service;

    @BeforeEach
    void setupMappers() {
      LocationMapper locationMapper = new LocationMapper(addressMapper);
      DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();
      ReflectionTestUtils.setField(bookingMapper, "locationMapper", locationMapper);
      ReflectionTestUtils.setField(bookingMapper, "displayedAddressMapper", displayedAddressMapper);
      ReflectionTestUtils.setField(bookingMapper, "requestedEquipmentGroupMapper", requestedEquipmentGroupMapper);
      ReflectionTestUtils.setField(bookingMapper, "commodityRequestedEquipmentLinkMapper", commodityRequestedEquipmentLinkMapper);
      ReflectionTestUtils.setField(requestedEquipmentGroupMapper, "activeReeferSettingsMapper", activeReeferSettingsMapper);
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
      assertFalse(bookingResult.valueAddedServiceRequests().isEmpty());
      assertFalse(bookingResult.references().isEmpty());
      assertFalse(bookingResult.requestedEquipments().isEmpty());
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
    @Mock private LocationService locationService;
    @Mock private VoyageService voyageService;
    @Mock private VesselService vesselService;
    @Mock private CommodityService commodityService;
    @Mock private ValueAddedServiceRequestService valueAddedServiceRequestService;
    @Mock private RequestedEquipmentGroupService requestedEquipmentGroupService;
    @Mock private ReferenceService referenceService;
    @Mock private DocumentPartyService documentPartyService;
    @Mock private ShipmentLocationService shipmentLocationService;
    @Mock private ModeOfTransportService modeOfTransportService;
    @Mock private CommodityRequestedEquipmentLinkService commodityRequestedEquipmentLinkService;

    @Mock private BookingRepository bookingRepository;
    @Mock private ShipmentEventRepository shipmentEventRepository;
    @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    @Spy private ModeOfTransportMapper modeOfTransportMapper = Mappers.getMapper(ModeOfTransportMapper.class);

    @InjectMocks private BookingService bookingService;

    @BeforeEach
    public void resetMocks() {
      reset(locationService, voyageService, vesselService, commodityService, valueAddedServiceRequestService,
        requestedEquipmentGroupService, referenceService, documentPartyService, shipmentLocationService,
        commodityRequestedEquipmentLinkService, bookingRepository, shipmentEventRepository);
    }

    @Test
    void testCreateFullBooking() {
      Voyage voyage = VoyageDataFactory.voyage();
      Vessel vessel = VesselDataFactory.vessel();
      ModeOfTransport modeOfTransport = ModeOfTransport.builder()
        .dcsaTransportType(DCSATransportType.VESSEL)
        .build();
      Location location = LocationDataFactory.addressLocationWithId();
      OffsetDateTime now = OffsetDateTime.now();

      BookingTO bookingRequest = BookingDataFactory.singleFullBookingRequestTO();
      Booking bookingToSave = bookingMapper.toDAO(bookingRequest).toBuilder()
        .vessel(vessel)
        .voyage(voyage)
        .placeOfIssue(location)
        .invoicePayableAt(location)
        .modeOfTransport(modeOfTransport)
        .build();
      Booking bookingSaved = bookingToSave.toBuilder()
        .carrierBookingRequestReference("carrierBookingRequestRef")
        .bookingRequestCreatedDateTime(now)
        .bookingRequestUpdatedDateTime(now)
        .documentStatus(BkgDocumentStatus.RECE)
        .build();

      when(voyageService.resolveVoyage(any())).thenReturn(voyage);
      when(vesselService.resolveVessel(any())).thenReturn(vessel);
      when(locationService.ensureResolvable(any())).thenReturn(location);
      when(bookingRepository.save(any())).thenReturn(bookingSaved);
      when(modeOfTransportService.resolveModeOfTransport(any())).thenReturn(modeOfTransport);

       // Execute
      BookingRefStatusTO result = bookingService.createBooking(bookingRequest);

      // Verify
      assertEquals("carrierBookingRequestRef", result.carrierBookingRequestReference());
      assertEquals(now, result.bookingRequestCreatedDateTime());
      assertEquals(now, result.bookingRequestUpdatedDateTime());
      assertEquals(org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE, result.documentStatus());

      ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
      ArgumentCaptor<ShipmentEvent> shipmentEventArgumentCaptor = ArgumentCaptor.forClass(ShipmentEvent.class);
      verify(voyageService).resolveVoyage(bookingRequest);
      verify(vesselService).resolveVessel(bookingRequest);
      verify(locationService, times(2)).ensureResolvable(bookingRequest.invoicePayableAt());
      verify(bookingRepository).save(bookingArgumentCaptor.capture());
      verify(shipmentEventRepository).save(shipmentEventArgumentCaptor.capture());
      verify(commodityService).createCommodities(eq(bookingRequest.commodities()), any(Booking.class), any());
      verify(valueAddedServiceRequestService).createValueAddedServiceRequests(eq(bookingRequest.valueAddedServiceRequests()), any(Booking.class));
      verify(referenceService).createReferences(eq(bookingRequest.references()), any(Booking.class));
      verify(requestedEquipmentGroupService).createRequestedEquipments(eq(bookingRequest.requestedEquipments()), any(Booking.class), any());
      verify(documentPartyService).createDocumentParties(eq(bookingRequest.documentParties()), any(Booking.class));
      verify(shipmentLocationService).createShipmentLocations(eq(bookingRequest.shipmentLocations()), any(Booking.class));

      Booking bookingActuallySaved = bookingArgumentCaptor.getValue();
      assertEquals(bookingToSave, bookingActuallySaved.toBuilder()
        .id(null)
        .carrierBookingRequestReference(null)
        .bookingRequestCreatedDateTime(null)
        .bookingRequestUpdatedDateTime(null)
        .documentStatus(null)
        .build());
      assertNotNull(bookingActuallySaved.getCarrierBookingRequestReference());
      assertNotNull(bookingActuallySaved.getBookingRequestCreatedDateTime());
      assertNotNull(bookingActuallySaved.getBookingRequestUpdatedDateTime());
      assertEquals(BkgDocumentStatus.RECE, bookingActuallySaved.getDocumentStatus());
      assertEquals(ShipmentEventTypeCode.RECE, shipmentEventArgumentCaptor.getValue().getShipmentEventTypeCode());
    }
  }
}
