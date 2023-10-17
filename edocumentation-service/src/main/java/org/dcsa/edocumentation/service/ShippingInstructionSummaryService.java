package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.domain.persistence.repository.specification.ShippingInstructionSpecification;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionSummaryMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingInstructionSummaryService extends AbstractSpecificationService<ShippingInstructionRepository, ShippingInstruction, UUID> {
  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionSummaryMapper shippingInstructionMapper;

  @Override
  protected ShippingInstructionRepository getRepository() {
    return shippingInstructionRepository;
  }

  @Transactional
  public PagedResult<ShippingInstructionSummaryTO> findShippingInstructionSummaries(
    PageRequest pageRequest,
    String documentStatus,
    @Nullable String carrierBookingReference) {
    return this.findViaComplexSpecificationWithLookup(
      ShippingInstructionSpecification.withFilters(
        ShippingInstructionSpecification.ShippingInstructionFilters.builder()
          .carrierBookingReference(
            carrierBookingReference == null
              ? null
              : Arrays.asList(carrierBookingReference.split(",")))
          .documentStatus(documentStatus)
          .build()),
      pageRequest,
      ShippingInstruction::getId,
      shippingInstructionMapper::shippingInstructionToShippingInstructionSummary
    );
  }
}
