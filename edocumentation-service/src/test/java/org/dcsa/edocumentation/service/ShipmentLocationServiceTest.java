package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.datafactories.ShipmentDataFactory;
import org.dcsa.edocumentation.datafactories.ShipmentLocationDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentLocationServiceTest {
  @Mock private LocationService locationService;
  @Mock private ShipmentLocationRepository shipmentLocationRepository;
  @Spy private ShipmentLocationMapper shipmentLocationMapper = Mappers.getMapper(ShipmentLocationMapper.class);
  @Mock private LocationMapper locationMapper;

  @InjectMocks ShipmentLocationService shipmentLocationService;

  @BeforeEach
  public void resetMocks() {
    reset(locationService, shipmentLocationRepository);

    ReflectionTestUtils.setField(shipmentLocationMapper, "locationMapper", locationMapper);
  }

  @Test
  void testCreateNull() {
    shipmentLocationService.createShipmentLocations(null, null);

    verify(locationService, never()).ensureResolvable(any());
    verify(shipmentLocationRepository, never()).saveAll(any());
    verify(shipmentLocationMapper, never()).toDAO(any(), any());
  }

  @Test
  void testCreateEmpty() {
    shipmentLocationService.createShipmentLocations(Collections.emptyList(), null);

    verify(locationService, never()).ensureResolvable(any());
    verify(shipmentLocationRepository, never()).saveAll(any());
    verify(shipmentLocationMapper, never()).toDAO(any(), any());
  }

  @Test
  void testCreate() {
    // Setup
    OffsetDateTime now = OffsetDateTime.now();
    Booking booking = BookingDataFactory.singleMinimalBooking();
    ShipmentLocationTO shipmentLocationTO = ShipmentLocationDataFactory.shipmentLocationTO(now);

    when(locationService.ensureResolvable(any())).thenReturn(LocationDataFactory.addressLocationWithId());

    // Execute
    shipmentLocationService.createShipmentLocations(List.of(shipmentLocationTO), booking);

    // Verify
    verify(locationService).ensureResolvable(shipmentLocationTO.location());
    verify(shipmentLocationRepository).saveAll(List.of(ShipmentLocationDataFactory.shipmentLocation(booking, now)));
  }

  @Test
  void shipmentLocationsTest_testGet() {
    when(shipmentLocationRepository.findByShipmentID(any())).thenReturn(Optional.of(ShipmentLocationDataFactory.singleShipmentLocation()));

    List<ShipmentLocationTO> response = shipmentLocationService.getShipmentLocations(List.of(ShipmentDataFactory.singleShipmentWithBooking()));

    assertEquals(1, response.size());
    assertEquals(ShipmentLocationTypeCode.OIR, response.get(0).shipmentLocationTypeCode());
  }
}
