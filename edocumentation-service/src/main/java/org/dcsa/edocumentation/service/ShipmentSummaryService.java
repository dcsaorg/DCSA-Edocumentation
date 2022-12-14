package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentSummaryMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShipmentSummaryService {
  private final ShipmentRepository shipmentRepository;
  private final ShipmentSummaryMapper shipmentSummaryMapper;
  private final DocumentStatusMapper documentStatusMapper;

  @Transactional
  public PagedResult<ShipmentSummaryTO> findShipmentSummaries(
      PageRequest pageRequest, BkgDocumentStatus documentStatus) {

    return new PagedResult<>(
        Optional.ofNullable(documentStatus)
            .map(documentStatusMapper::toDomainBkgDocumentStatus)
            .map(
                bkgDocumentStatus ->
                    shipmentRepository.findAllByBookingDocumentStatus(
                        bkgDocumentStatus, pageRequest))
            .orElseGet(() -> shipmentRepository.findAll(pageRequest))
            .map(shipmentSummaryMapper::shipmentToShipmentSummary));
  }
}
