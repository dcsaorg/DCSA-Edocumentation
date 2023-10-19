package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.infra.enums.BookingStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRequestRepository;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingRequestSummaryServiceTest {

  @Mock private BookingRequestRepository bookingRequestRepository;

  @Spy
  private BookingSummaryMapper bookingSummaryMapper = Mappers.getMapper(BookingSummaryMapper.class);

  @InjectMocks private BookingSummaryService bookingSummaryService;

  private final PageRequest mockPageRequest = PageRequest.of(1, 1);

  @Test
  void testBookingSummary_singleResultNoDocumentStatus() {
    BookingRequest mockBookingRequest = BookingDataFactory.singleShallowBookingWithVesselAndModeOfTransport();
    Page<BookingRequest> pagedResult = new PageImpl<>(List.of(mockBookingRequest));
    when(bookingRequestRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockPageRequest, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBookingRequest.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBookingRequest.getVessel().getVesselIMONumber(), result.content().get(0).vesselIMONumber());
  }

  @Test
  void testBookingSummary_singleResultWithDocumentStatus() {
    BookingRequest mockBookingRequest = BookingDataFactory.singleShallowBookingWithVesselAndModeOfTransport();
    Page<BookingRequest> pagedResult = new PageImpl<>(List.of(mockBookingRequest));
    when(bookingRequestRepository.findAllByBookingStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(
            mockPageRequest, BookingStatus.RECEIVED);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBookingRequest.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_multipleResultsWithoutDocumentStatus() {
    List<BookingRequest> mockBookingRequests =
        BookingDataFactory.multipleShallowBookingsWithVesselAndModeOfTransport();
    Page<BookingRequest> pagedResult = new PageImpl<>(mockBookingRequests);
    when(bookingRequestRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockPageRequest, null);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockBookingRequests.get(0).getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBookingRequests.get(1).getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_multipleResultsWithDocumentStatus() {
    List<BookingRequest> mockBookingRequests =
        BookingDataFactory.multipleShallowBookingsWithVesselAndModeOfTransport();
    Page<BookingRequest> pagedResult = new PageImpl<>(mockBookingRequests);
    when(bookingRequestRepository.findAllByBookingStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(
            mockPageRequest, BookingStatus.RECEIVED);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockBookingRequests.get(0).getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockBookingRequests.get(1).getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }

  @Test
  void testBookingSummary_singleResultWithoutDocumentStatusWithoutVessel() {
    BookingRequest mockBookingRequest = BookingDataFactory.singleShallowBooking();
    Page<BookingRequest> pagedResult = new PageImpl<>(List.of(mockBookingRequest));
    when(bookingRequestRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockPageRequest, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBookingRequest.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertNull(result.content().get(0).vesselIMONumber());
  }
}
