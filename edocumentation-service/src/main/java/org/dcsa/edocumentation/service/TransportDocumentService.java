package org.dcsa.edocumentation.service;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentSpecification;
import org.dcsa.edocumentation.service.mapping.TransportDocumentSummaryMapper;
import org.dcsa.edocumentation.transferobjects.TransportDocumentSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentSpecification.withFilters;

@RequiredArgsConstructor
@Service
public class TransportDocumentService {

  private final TransportDocumentRepository transportDocumentRepository;
  private final TransportDocumentSummaryMapper transportDocumentSummaryMapper;

  @Builder
  @Getter
  public static class Filters {
    private String carrierBookingReference;
    private EblDocumentStatus documentStatus;
    private PageRequest pageRequest;
  }

  public PagedResult<TransportDocumentSummaryTO> getTransportDocumentSummaries(
      final Filters filters) {

    Page<TransportDocument> transportDocuments =
        transportDocumentRepository.findAll(
            withFilters(
                TransportDocumentSpecification.Filters.builder()
                    .carrierBookingReference(
                      filters.carrierBookingReference == null
                        ? null
                        : Arrays.asList(filters.carrierBookingReference.split(",")))
                    .documentStatus(
                        null != filters.documentStatus
                            ? org.dcsa.edocumentation.domain.persistence.entity.enums
                                .EblDocumentStatus.valueOf(filters.documentStatus.toString())
                            : null)
                    .build()),
            filters.pageRequest);

    return new PagedResult<>(transportDocuments, transportDocumentSummaryMapper::toTO);
  }
}
