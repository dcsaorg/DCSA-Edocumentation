package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingSummaryService {
  private final BookingRepository repository;
  private final BookingSummaryMapper bookingSummaryMapper;
  private final DocumentStatusMapper documentStatusMapper;

  public PagedResult<BookingSummaryTO> findBookingSummaries(
      Cursor cursor, BkgDocumentStatus documentStatus) {

    return new PagedResult<>(
        Optional.ofNullable(documentStatus)
            .map(documentStatusMapper::toDomainBkgDocumentStatus)
            .map(
                bkgDocumentStatus ->
                    repository.findAllByDocumentStatus(bkgDocumentStatus, cursor.toPageRequest()))
            .orElseGet(() -> repository.findAll(cursor.toPageRequest()))
            .map(bookingSummaryMapper::BookingToBookingSummary));
  }
}
