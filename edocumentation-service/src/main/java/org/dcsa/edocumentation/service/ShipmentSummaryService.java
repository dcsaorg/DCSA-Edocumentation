package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentSummaryMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentSummaryService {
  private final ShipmentRepository shipmentRepository;
  private final ShipmentSummaryMapper shipmentSummaryMapper;
  private final DocumentStatusMapper documentStatusMapper;

  public PagedResult<ShipmentSummaryTO> findShipmentSummaries(
      Cursor cursor, BkgDocumentStatus documentStatus) {

    return new PagedResult<>(
        Optional.ofNullable(documentStatus)
            .map(documentStatusMapper::toDomainBkgDocumentStatus)
            .map(
                bkgDocumentStatus ->
                    shipmentRepository.findAllByBookingDocumentStatus(
                        bkgDocumentStatus, cursor.toPageRequest()))
            .orElseGet(() -> shipmentRepository.findAll(cursor.toPageRequest()))
            .map(shipmentSummaryMapper::ShipmentToShipmentSummary));
  }
}
