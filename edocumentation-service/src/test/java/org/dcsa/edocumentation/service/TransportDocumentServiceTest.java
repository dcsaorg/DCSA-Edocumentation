package org.dcsa.edocumentation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.dcsa.edocumentation.datafactories.TransportDocumentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.mapping.TransportDocumentMapper;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
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

@ExtendWith(MockitoExtension.class)
class TransportDocumentServiceTest {

  @Mock TransportDocumentRepository transportDocumentRepository;

  @Mock LocationMapper locationMapper;

  @Spy
  TransportDocumentMapper transportDocumentMapper =
      Mappers.getMapper(TransportDocumentMapper.class);

  @InjectMocks TransportDocumentService transportDocumentService;

  @BeforeEach
  void init() {
    ReflectionTestUtils.setField(transportDocumentMapper, "locationMapper", locationMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "locMapper", locationMapper);
  }

  @Test
  void transportDocumentServiceTest_testGetTransportDocument() {
    Optional<TransportDocument> expected =
        Optional.of(
            TransportDocumentDataFactory.singleTransportDocument());

    when(transportDocumentRepository.findByTransportDocumentReferenceAndValidUntilIsNull(any()))
        .thenReturn(expected);

    Optional<TransportDocumentTO> transportDocumentTOOptional =
        transportDocumentService.findByReference("TestReference");

    assertTrue(transportDocumentTOOptional.isPresent());
    TransportDocumentTO transportDocumentTO = transportDocumentTOOptional.get();
    assertNotNull(transportDocumentTO.transports());
  }
}
