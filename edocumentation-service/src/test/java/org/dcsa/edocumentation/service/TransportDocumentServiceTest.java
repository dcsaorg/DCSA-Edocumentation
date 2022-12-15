package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ShipmentLocationDataFactory;
import org.dcsa.edocumentation.datafactories.TransportDataFactory;
import org.dcsa.edocumentation.datafactories.TransportDocumentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.skernel.domain.persistence.entity.Carrier;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransportDocumentServiceTest {

  @Mock TransportDocumentRepository transportDocumentRepository;
  @Mock TransportService transportService;
  @Mock ShipmentLocationService shipmentLocationService;

  @Mock LocationMapper locationMapper;
  @Mock ShippingInstructionMapper shippingInstructionMapper;

  @Spy
  TransportDocumentMapper transportDocumentMapper =
      Mappers.getMapper(TransportDocumentMapper.class);

  @InjectMocks TransportDocumentService transportDocumentService;

  @BeforeEach
  void init() {
    ReflectionTestUtils.setField(transportDocumentMapper, "locationMapper", locationMapper);
    ReflectionTestUtils.setField(
        transportDocumentMapper, "shippingInstructionMapper", shippingInstructionMapper);
  }

  @Test
  void transportDocumentServiceTest_testGetTransportDocument() {
    Optional<TransportDocument> expected =
        Optional.of(
            TransportDocumentDataFactory.singleTransportDocument());
    List<TransportTO> expectedTransports = List.of(TransportDataFactory.singleTransportTO());
    List<ShipmentLocationTO> expectedShipmentLocations =
        List.of(ShipmentLocationDataFactory.shipmentLocationTO());

    when(transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(any()))
        .thenReturn(expected);
    when(transportService.findTransportByShipmentId(any())).thenReturn(expectedTransports);
    when(shipmentLocationService.getShipmentLocations(any())).thenReturn(expectedShipmentLocations);

    Optional<TransportDocumentTO> transportDocumentTOOptional =
        transportDocumentService.findByReference("TestReference");

    assertTrue(transportDocumentTOOptional.isPresent());
    TransportDocumentTO transportDocumentTO = transportDocumentTOOptional.get();
    assertEquals(1, transportDocumentTO.transports().get(0).transportPlanStageSequenceNumber());
    assertEquals("Displayed name", transportDocumentTO.shipmentLocations().get(0).displayedName());
  }

  @Test
  void transportDocumentServiceTest_testNoShippingInstruction() {
    Optional<TransportDocument> expected = Optional.of(TransportDocument.builder()
      .id(UUID.fromString("9349a7e2-85cd-4f96-8229-d7fa8c5fc427"))
      .carrier(Carrier.builder()
        .carrierName("Dummy Carrier")
        .smdgCode("DMY")
        .build())
      .build());

    when(transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(any()))
      .thenReturn(expected);
    ConcreteRequestErrorMessageException exceptionCaught =
      assertThrows(
        ConcreteRequestErrorMessageException.class,
        () -> transportDocumentService.findByReference("TestReference"));

    assertEquals("No shipping instruction present for this transportDocument.", exceptionCaught.getMessage());
  }

  @Test
  void transportDocumentServiceTest_testNoCarrierFound() {
    Optional<TransportDocument> expected = Optional.of(TransportDocument.builder().build());

    when(transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(any()))
        .thenReturn(expected);

    ConcreteRequestErrorMessageException exceptionCaught =
        assertThrows(
            ConcreteRequestErrorMessageException.class,
            () -> transportDocumentService.findByReference("TestReference"));

    assertEquals("No Carrier found on the transportdocument.", exceptionCaught.getMessage());
  }
}
