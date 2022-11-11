package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.*;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

  @Mock private BookingRepository repository;
  @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
  @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
  @Spy private RequestedEquipmentMapper requestedEquipmentMapper = Mappers.getMapper(RequestedEquipmentMapper.class);

  @InjectMocks private BookingService service;

  @BeforeEach
  void setupMappers() {
    LocationMapper locationMapper = new LocationMapper(addressMapper);
    DisplayedAddressMapper displayedAddressMapper = new DisplayedAddressMapper();
    ReflectionTestUtils.setField(bookingMapper, "locationMapper", locationMapper);
    ReflectionTestUtils.setField(bookingMapper, "displayedAddressMapper", displayedAddressMapper);
    ReflectionTestUtils.setField(bookingMapper, "requestedEquipmentMapper", requestedEquipmentMapper);
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
