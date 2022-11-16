package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.BookingRepository;
import org.dcsa.edocumentation.service.mapping.BookingSummaryMapper;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.transferobjects.BookingSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingSummaryService {
  private final BookingRepository repository;
  private final BookingSummaryMapper bookingSummaryMapper;
  private final DocumentStatusMapper documentStatusMapper;

  @Transactional
  public PagedResult<BookingSummaryTO> findBookingSummaries(
    PageRequest pageRequest, BkgDocumentStatus documentStatus) {

    return new PagedResult<>(
        Optional.ofNullable(documentStatus)
            .map(documentStatusMapper::toDomainBkgDocumentStatus)
            .map(
                bkgDocumentStatus ->
                    repository.findAllByDocumentStatus(bkgDocumentStatus, pageRequest))
            .orElseGet(() -> repository.findAll(pageRequest))
            .map(bookingSummaryMapper::BookingToBookingSummary));
  }
}
