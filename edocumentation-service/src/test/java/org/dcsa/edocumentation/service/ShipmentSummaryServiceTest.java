package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ShipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.DocumentStatusMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentSummaryMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentSummaryTO;
import org.dcsa.skernel.infrastructure.pagination.Cursor;
import org.dcsa.skernel.infrastructure.pagination.PagedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShipmentSummaryServiceTest {
  @Mock private ShipmentRepository shipmentRepository;

  @Spy
  private DocumentStatusMapper documentStatusMapper = Mappers.getMapper(DocumentStatusMapper.class);

  @Spy
  private ShipmentSummaryMapper shipmentSummaryMapper =
      Mappers.getMapper(ShipmentSummaryMapper.class);

  @InjectMocks private ShipmentSummaryService shipmentSummaryService;
  private Cursor mockCursor;

  @BeforeEach
  void init() {
    mockCursor =
        new Cursor(1, 1, new Cursor.SortBy(Sort.Direction.ASC, "carrierBookingRequestReference"));
  }

  @Test
  void testShipmentSummary_singleResultNoDocumentStatusWithoutBooking() {
    Shipment mockShipment = ShipmentDataFactory.singleShipmentWithoutBooking();
    Page<Shipment> pagedResult = new PageImpl<>(List.of(mockShipment));
    when(shipmentRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<ShipmentSummaryTO> result =
        shipmentSummaryService.findShipmentSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockShipment.getCarrierBookingReference(),
        result.content().get(0).carrierBookingReference());
    assertEquals(
        mockShipment.getShipmentUpdatedDateTime(),
        result.content().get(0).shipmentUpdatedDateTime());
  }

  @Test
  void testShipmentSummary_singleResultNoDocumentStatusWithBooking() {
    Shipment mockShipment = ShipmentDataFactory.singleShipmentWithBooking();
    Page<Shipment> pagedResult = new PageImpl<>(List.of(mockShipment));
    when(shipmentRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<ShipmentSummaryTO> result =
        shipmentSummaryService.findShipmentSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockShipment.getCarrierBookingReference(),
        result.content().get(0).carrierBookingReference());
    assertEquals(
        mockShipment.getBooking().getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
  }

  @Test
  void testShipmentSummary_singleResultWithDocumentStatusWithBooking() {
    Shipment mockShipment = ShipmentDataFactory.singleShipmentWithBooking();
    Page<Shipment> pagedResult = new PageImpl<>(List.of(mockShipment));
    when(shipmentRepository.findAllByBookingDocumentStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<ShipmentSummaryTO> result =
        shipmentSummaryService.findShipmentSummaries(
            mockCursor, org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE);
    assertEquals(1, result.totalPages());
    assertEquals(
        mockShipment.getCarrierBookingReference(),
        result.content().get(0).carrierBookingReference());
    assertEquals(
        mockShipment.getBooking().getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
  }

  @Test
  void testShipmentSummary_multipleResultsNoDocumentStatusWithBooking() {
    List<Shipment> mockShipments = ShipmentDataFactory.multipleShipmentsWithBooking();
    Page<Shipment> pagedResult = new PageImpl<>(mockShipments);
    when(shipmentRepository.findAll(any(Pageable.class))).thenReturn(pagedResult);

    PagedResult<ShipmentSummaryTO> result =
        shipmentSummaryService.findShipmentSummaries(mockCursor, null);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockShipments.get(0).getCarrierBookingReference(),
        result.content().get(0).carrierBookingReference());
    assertEquals(
        mockShipments.get(1).getCarrierBookingReference(),
        result.content().get(1).carrierBookingReference());
    assertEquals(
        mockShipments.get(0).getBooking().getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockShipments.get(1).getBooking().getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }

  @Test
  void testShipmentSummary_multipleResultsWithDocumentStatusWithBooking() {
    List<Shipment> mockShipments = ShipmentDataFactory.multipleShipmentsWithBooking();
    Page<Shipment> pagedResult = new PageImpl<>(mockShipments);
    when(shipmentRepository.findAllByBookingDocumentStatus(any(), any(Pageable.class)))
        .thenReturn(pagedResult);

    PagedResult<ShipmentSummaryTO> result =
        shipmentSummaryService.findShipmentSummaries(
            mockCursor, org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus.RECE);
    assertEquals(1, result.totalPages());
    assertEquals(2, result.content().size());
    assertEquals(
        mockShipments.get(0).getCarrierBookingReference(),
        result.content().get(0).carrierBookingReference());
    assertEquals(
        mockShipments.get(1).getCarrierBookingReference(),
        result.content().get(1).carrierBookingReference());
    assertEquals(
        mockShipments.get(0).getBooking().getCarrierBookingRequestReference(),
        result.content().get(0).carrierBookingRequestReference());
    assertEquals(
        mockShipments.get(1).getBooking().getCarrierBookingRequestReference(),
        result.content().get(1).carrierBookingRequestReference());
  }
}
