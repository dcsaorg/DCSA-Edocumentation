package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.repository.specification.ShippingInstructionSpecification;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;

  public PagedResult<ShippingInstructionSummaryTO> findShippingInstructionSummaries(
      Cursor cursor, EblDocumentStatus documentStatus, @Nullable String carrierBookingReference) {
    return new PagedResult<>(
        shippingInstructionRepository
            .findAll(
                ShippingInstructionSpecification.withFilters(
                    ShippingInstructionSpecification.ShippingInstructionFilters.builder()
                        .carrierBookingReference(
                            carrierBookingReference == null
                                ? null
                                : Arrays.asList(carrierBookingReference.split(",")))
                        .documentStatus(documentStatus)
                        .build()),
                cursor.toPageRequest())
            .map(shippingInstructionMapper::ShippingInstructionToShippingInstructionSummary));
  }
}
