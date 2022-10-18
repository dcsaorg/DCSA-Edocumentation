package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingSummaryServiceTest {

  @Mock private BookingRepository bookingRepository;

  @Spy
  private BookingSummaryMapper bookingSummaryMapper = Mappers.getMapper(BookingSummaryMapper.class);

  @Spy
  private DocumentStatusMapper documentStatusMapper = Mappers.getMapper(DocumentStatusMapper.class);

  @InjectMocks private BookingSummaryService bookingSummaryService;

  private Cursor mockCursor;

  @BeforeEach
  void init() {
    mockCursor =
        new Cursor(1, 1, new Cursor.SortBy(Sort.Direction.ASC, "carrierBookingRequestReference"));
  }

  @Test
  void testBookingSummary_singleResultNoDocumentStatus() {
    Booking mockBooking = BookingDataFactory.singleShallowBookingWithVessel();
    Page<Booking> pagedResult = new PageImpl<>(List.of(mockBooking));
    when(bookingRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBooking.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBooking.getVessel().getVesselIMONumber(), result.content().get(0).vesselIMONumber());
  }

  @Test
  void testBookingSummary_singleResultWithDocumentStatus() {
    Booking mockBooking = BookingDataFactory.singleShallowBookingWithVessel();
    Page<Booking> pagedResult = new PageImpl<>(List.of(mockBooking));
    when(bookingRepository.findAllByDocumentStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(
            mockCursor, org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBooking.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_multipleResultsWithoutDocumentStatus() {
    List<Booking> mockBookings = BookingDataFactory.multipleShallowBookingsWithVessel();
    Page<Booking> pagedResult = new PageImpl<>(mockBookings);
    when(bookingRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockBookings.get(0).getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBookings.get(1).getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_multipleResultsWithDocumentStatus() {
    List<Booking> mockBookings = BookingDataFactory.multipleShallowBookingsWithVessel();
    Page<Booking> pagedResult = new PageImpl<>(mockBookings);
    when(bookingRepository.findAllByDocumentStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(
            mockCursor, org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockBookings.get(0).getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBookings.get(1).getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_singleResultWithoutDocumentStatusWithoutVessel() {
    Booking mockBooking = BookingDataFactory.singleShallowBooking();
    Page<Booking> pagedResult = new PageImpl<>(List.of(mockBooking));
    when(bookingRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBooking.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertNull(result.content().get(0).vesselIMONumber());
  }
}
