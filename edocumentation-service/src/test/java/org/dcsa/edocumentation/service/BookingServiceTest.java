package org.dcsa.edocumentation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DocumentTypeCode;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EventClassifierCode;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.dcsa.skernel.infrastructure.jackson.JacksonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
  @Mock private org.dcsa.edocumentation.domain.decoupled.repository.BookingRepository bookingRepository;
  @Mock private org.dcsa.edocumentation.domain.decoupled.repository.ShipmentEventRepository shipmentEventRepository;
  @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
  @Spy private ObjectMapper objectMapper = new JacksonConfiguration().defaultObjectMapper();

  @InjectMocks private BookingService bookingService;

  @BeforeEach
  public void setup() {
    reset(bookingRepository, shipmentEventRepository);
    ReflectionTestUtils.setField(bookingMapper, "objectMapper", objectMapper);
  }

  @Test
  public void testGetBooking() {
    BookingTO bookingTO = BookingDataFactory.singleFullBookingRequestTO();
    Booking booking = bookingMapper.toDAO(bookingTO);
    when(bookingRepository.findByCarrierBookingRequestReference(any())).thenReturn(Optional.of(booking));

    BookingTO actual = bookingService.getBooking("something");

    assertEquals(nullGenerated(bookingTO), nullGenerated(actual));
  }

  @Test
  public void testGetBooking_NotFound() {
    when(bookingRepository.findByCarrierBookingRequestReference(any())).thenReturn(Optional.empty());

    NotFoundException exception = assertThrows(NotFoundException.class, () -> bookingService.getBooking("something"));
    assertEquals("No booking found with carrierBookingRequestReference: something", exception.getMessage());
  }

  @Test
  public void testCreateBooking() {
    // Setup
    UUID bookingId = UUID.randomUUID();
    UUID shipmentEventId = UUID.randomUUID();
    BookingTO bookingRequest = BookingDataFactory.singleFullBookingRequestTO();
    when(bookingRepository.save(any())).thenAnswer(invocation -> ((Booking)invocation.getArguments()[0]).toBuilder().id(bookingId).build());
    when(shipmentEventRepository.save(any())).thenAnswer(invocation -> ((ShipmentEvent)invocation.getArguments()[0]).toBuilder().eventId(shipmentEventId).build());

    // Execute
    BookingRefStatusTO result = bookingService.createBooking(bookingRequest);

    // Verify
    ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
    ArgumentCaptor<ShipmentEvent> shipmentEventCaptor = ArgumentCaptor.forClass(ShipmentEvent.class);
    verify(bookingRepository).save(bookingCaptor.capture());
    verify(shipmentEventRepository).save(shipmentEventCaptor.capture());
    assertBookingAndEventSaved(bookingRequest, bookingCaptor.getValue(), shipmentEventCaptor.getValue(), null);
  }

  @SneakyThrows
  private void assertBookingAndEventSaved(BookingTO bookingTO, Booking booking, ShipmentEvent shipmentEvent, String reason) {
    // assertions on Booking
    assertNull(booking.getValidUntil());
    assertNotNull(booking.getContent());
    BookingTO embeddedBookingTO = objectMapper.readValue(booking.getContent(), BookingTO.class);

    assertEquals(nullGenerated(bookingTO), nullGenerated(embeddedBookingTO));

    assertNotNull(embeddedBookingTO.carrierBookingRequestReference());
    assertEquals(embeddedBookingTO.carrierBookingRequestReference(), booking.getCarrierBookingRequestReference());

    assertNotNull(embeddedBookingTO.bookingRequestCreatedDateTime());
    assertEquals(embeddedBookingTO.bookingRequestCreatedDateTime().toInstant(), booking.getBookingRequestCreatedDateTime().toInstant());

    assertNotNull(embeddedBookingTO.bookingRequestUpdatedDateTime());
    assertEquals(embeddedBookingTO.bookingRequestUpdatedDateTime().toInstant(), booking.getBookingRequestUpdatedDateTime().toInstant());

    assertNotNull(embeddedBookingTO.documentStatus());
    assertEquals(bookingMapper.toDAO(embeddedBookingTO.documentStatus()), booking.getDocumentStatus());

    // assertions on ShipmentEvent
    assertEquals(booking.getCarrierBookingRequestReference(), shipmentEvent.getDocumentReference());
    assertEquals(DocumentTypeCode.CBR, shipmentEvent.getDocumentTypeCode());
    assertEquals(booking.getDocumentStatus().asShipmentEventTypeCode(), shipmentEvent.getShipmentEventTypeCode());
    assertEquals(reason, shipmentEvent.getReason());
    assertEquals(EventClassifierCode.ACT, shipmentEvent.getEventClassifierCode());
    assertEquals(booking.getBookingRequestUpdatedDateTime(), shipmentEvent.getEventDateTime());
    assertEquals(shipmentEvent.getEventDateTime(), shipmentEvent.getEventCreatedDateTime());
  }

  private BookingTO nullGenerated(BookingTO bookingTO) {
    return bookingTO.toBuilder()
      .carrierBookingRequestReference(null)
      .documentStatus(null)
      .bookingRequestCreatedDateTime(null)
      .bookingRequestUpdatedDateTime(null)
      .shipmentLocations(bookingTO.shipmentLocations().stream()
        .map(shipmentLocation -> shipmentLocation.toBuilder().eventDateTime(null).build())
        .toList())
      .build();
  }
}
