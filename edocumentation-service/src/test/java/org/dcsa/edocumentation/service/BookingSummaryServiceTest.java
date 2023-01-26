package org.dcsa.edocumentation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.domain.decoupled.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.infrastructure.jackson.JacksonConfiguration;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingSummaryServiceTest {

  @Mock private BookingRepository bookingRepository;

  @Spy private BookingSummaryMapper bookingSummaryMapper = Mappers.getMapper(BookingSummaryMapper.class);
  @Spy private DocumentStatusMapper documentStatusMapper = Mappers.getMapper(DocumentStatusMapper.class);
  @Spy private BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
  @Spy private ObjectMapper objectMapper = new JacksonConfiguration().defaultObjectMapper();

  @InjectMocks private BookingSummaryService bookingSummaryService;

  private final PageRequest mockPageRequest = PageRequest.of(1, 1);

  @BeforeEach
  public void setup() {
    reset(bookingRepository);
    ReflectionTestUtils.setField(bookingMapper, "objectMapper", objectMapper);
  }

  @Test
  void testBookingSummary_singleResultNoDocumentStatus() {
    BookingTO bookingTO = BookingDataFactory.singleFullBookingRequestTO();
    Booking mockBooking = bookingMapper.toDAO(bookingTO);
    Page<Booking> pagedResult = new PageImpl<>(List.of(mockBooking));
    when(bookingRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(mockPageRequest, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBooking.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        bookingTO.vesselIMONumber(), result.content().get(0).vesselIMONumber());
  }

  @Test
  void testBookingSummary_singleResultWithDocumentStatus() {
    BookingTO bookingTO = BookingDataFactory.singleFullBookingRequestTO();
    Booking mockBooking = bookingMapper.toDAO(bookingTO);
    Page<Booking> pagedResult = new PageImpl<>(List.of(mockBooking));
    when(bookingRepository.findAllByDocumentStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<BookingSummaryTO> result =
        bookingSummaryService.findBookingSummaries(
            mockPageRequest, org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockBooking.getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
  }
}
