package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;

  @Transactional
  public BookingRefStatusTO createBooking(BookingTO bookingRequest) {
    OffsetDateTime now = OffsetDateTime.now();
    Booking booking = bookingRepository.save(
      bookingMapper.toDAO(bookingRequest).toBuilder()
        .carrierBookingRequestReference(UUID.randomUUID().toString())
        .documentStatus(BkgDocumentStatus.RECE)
        .bookingRequestCreatedDateTime(now)
        .bookingRequestUpdatedDateTime(now)
        .build()
    );

    return bookingMapper.toStatusDTO(booking);
  }

  @Transactional
  public BookingRefStatusTO updateBooking(String carrierBookingRequestReference, BookingTO bookingRequest) {
    return null; // TODO https://dcsa.atlassian.net/browse/DDT-1277
  }

  @Transactional
  public BookingTO getBooking(String carrierBookingRequestReference) {
    return null; // TODO https://dcsa.atlassian.net/browse/DDT-1273
  }
}
