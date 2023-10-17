package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingSummaryService {
  private final BookingRepository repository;
  private final BookingSummaryMapper bookingSummaryMapper;

  @Transactional
  public PagedResult<BookingSummaryTO> findBookingSummaries(
    PageRequest pageRequest, String bookingStatus) {

    return new PagedResult<>(
        Optional.ofNullable(bookingStatus)
            .map(
              bookingStatus2 ->
                    repository.findAllByBookingStatus(bookingStatus2, pageRequest))
            .orElseGet(() -> repository.findAll(pageRequest))
            .map(bookingSummaryMapper::BookingToBookingSummary));
  }
}
