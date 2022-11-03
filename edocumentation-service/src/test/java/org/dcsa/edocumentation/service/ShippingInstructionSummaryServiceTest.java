package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ShippingInstructionDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionSummaryMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShippingInstructionSummaryServiceTest {

  @Mock private ShippingInstructionRepository shippingInstructionRepository;

  @Spy
  private DocumentStatusMapper documentStatusMapper = Mappers.getMapper(DocumentStatusMapper.class);

  @Spy
  private ShippingInstructionSummaryMapper shippingInstructionSummaryMapper =
      Mappers.getMapper(ShippingInstructionSummaryMapper.class);

  @InjectMocks private ShippingInstructionSummaryService service;

  private final PageRequest mockPageRequest = PageRequest.of(1, 1);

  @Test
  void testShippingInstructionSummary_singleShallowResult() {
    ShippingInstruction mockShippingInstruction =
        ShippingInstructionDataFactory.singleShallowShippingInstruction();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(List.of(mockShippingInstruction));
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
        service.findShippingInstructionSummaries(mockPageRequest, null, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockShippingInstruction.getShippingInstructionReference(),
        result.content().get(0).shippingInstructionReference());
    assertEquals(
        mockShippingInstruction.getShippingInstructionCreatedDateTime(),
        result.content().get(0).shippingInstructionCreatedDateTime());
    assertNull(mockShippingInstruction.getShipments());
  }

  @Test
  void testShippingInstructionSummary_singleShallowResultWithDocumentStatus() {
    ShippingInstruction mockShippingInstruction =
      ShippingInstructionDataFactory.singleShallowShippingInstruction();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(List.of(mockShippingInstruction));
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
      .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
      service.findShippingInstructionSummaries(mockPageRequest, EblDocumentStatus.RECE, null);
    assertEquals(1, result.totalPages());
    assertEquals(
      mockShippingInstruction.getShippingInstructionReference(),
      result.content().get(0).shippingInstructionReference());
    assertEquals(
      mockShippingInstruction.getShippingInstructionCreatedDateTime(),
      result.content().get(0).shippingInstructionCreatedDateTime());
    assertNull(mockShippingInstruction.getShipments());
  }

  @Test
  void testShippingInstructionSummary_singleShallowResultWithShipment() {
    ShippingInstruction mockShippingInstruction =
        ShippingInstructionDataFactory
            .singleShallowShippingInstructionWithPlaceOfIssueAndShipments();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(List.of(mockShippingInstruction));
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
        service.findShippingInstructionSummaries(mockPageRequest, null, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockShippingInstruction.getShippingInstructionReference(),
        result.content().get(0).shippingInstructionReference());
    assertEquals(
        mockShippingInstruction.getShippingInstructionCreatedDateTime(),
        result.content().get(0).shippingInstructionCreatedDateTime());
    assertEquals(
        mockShippingInstruction.getShipments().stream()
            .toList()
            .get(0)
            .getCarrierBookingReference(),
        result.content().get(0).carrierBookingReferences().get(0));
  }

  @Test
  void testShippingInstructionSummary_singleShallowResultWithShipmentWithCarrierBookingReference() {
    ShippingInstruction mockShippingInstruction =
      ShippingInstructionDataFactory
        .singleShallowShippingInstructionWithPlaceOfIssueAndShipments();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(List.of(mockShippingInstruction));
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
      .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
      service.findShippingInstructionSummaries(mockPageRequest, null, UUID.randomUUID().toString());
    assertEquals(1, result.totalPages());
    assertEquals(
      mockShippingInstruction.getShippingInstructionReference(),
      result.content().get(0).shippingInstructionReference());
    assertEquals(
      mockShippingInstruction.getShippingInstructionCreatedDateTime(),
      result.content().get(0).shippingInstructionCreatedDateTime());
    assertEquals(
      mockShippingInstruction.getShipments().stream()
        .toList()
        .get(0)
        .getCarrierBookingReference(),
      result.content().get(0).carrierBookingReferences().get(0));
  }

  @Test
  void testShippingInstructionSummary_singleShallowResultWithShipmentWithCarrierBookingReferenceAndDocumentStatus() {
    ShippingInstruction mockShippingInstruction =
      ShippingInstructionDataFactory
        .singleShallowShippingInstructionWithPlaceOfIssueAndShipments();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(List.of(mockShippingInstruction));
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
      .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
      service.findShippingInstructionSummaries(mockPageRequest, EblDocumentStatus.RECE, UUID.randomUUID().toString());
    assertEquals(1, result.totalPages());
    assertEquals(
      mockShippingInstruction.getShippingInstructionReference(),
      result.content().get(0).shippingInstructionReference());
    assertEquals(
      mockShippingInstruction.getShippingInstructionCreatedDateTime(),
      result.content().get(0).shippingInstructionCreatedDateTime());
    assertEquals(
      mockShippingInstruction.getShipments().stream()
        .toList()
        .get(0)
        .getCarrierBookingReference(),
      result.content().get(0).carrierBookingReferences().get(0));
  }

  @Test
  void testShippingInstructionSummary_multipleShallowResultsWithShipment() {
    List<ShippingInstruction> mockShippingInstruction =
      ShippingInstructionDataFactory
        .multipleShallowShippingInstructionWithPlaceOfIssueAndShipments();
    Page<ShippingInstruction> pagedResult = new PageImpl<>(mockShippingInstruction);
    when(shippingInstructionRepository.findAll(any(Specification.class), any(Pageable.class)))
      .thenReturn(pagedResult);

    PagedResult<ShippingInstructionSummaryTO> result =
      service.findShippingInstructionSummaries(mockPageRequest, null, null);
    assertEquals(1, result.totalPages());
    assertEquals(
      mockShippingInstruction.get(0).getShippingInstructionReference(),
      result.content().get(0).shippingInstructionReference());
    assertEquals(
      mockShippingInstruction.get(0).getShippingInstructionCreatedDateTime(),
      result.content().get(0).shippingInstructionCreatedDateTime());
    assertEquals(
      mockShippingInstruction.get(0).getShipments().stream()
        .toList()
        .get(0)
        .getCarrierBookingReference(),
      result.content().get(0).carrierBookingReferences().get(0));
  }
}
