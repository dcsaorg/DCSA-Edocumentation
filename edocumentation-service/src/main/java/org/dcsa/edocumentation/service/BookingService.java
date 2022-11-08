package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingMapper;
import org.dcsa.edocumentation.transferobjects.BookingRefStatusTO;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
  private final BookingRepository bookingRepository;
  private final BookingMapper bookingMapper;

  @Transactional
  public BookingRefStatusTO createBooking(BookingTO bookingTO) {
    return BookingRefStatusTO.builder()
        .carrierBookingRequestReference(UUID.randomUUID().toString())
        .documentStatus(BkgDocumentStatus.RECE)
        .bookingRequestCreatedDateTime(OffsetDateTime.now())
        .bookingRequestUpdatedDateTime(OffsetDateTime.now())
        .build();
  }

  public Optional<BookingTO> getBooking(String carrierBookingRequestReference) {
    return Optional.ofNullable(
            bookingRepository.findBookingByCarrierBookingRequestReference(
                carrierBookingRequestReference))
        .map(bookingMapper::bookingToBookingTO);
  }
}
