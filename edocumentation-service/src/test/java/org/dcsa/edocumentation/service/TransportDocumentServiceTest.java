package org.dcsa.edocumentation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.dcsa.edocumentation.datafactories.TransportDocumentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.domain.persistence.repository.TransportDocumentRepository;
import org.dcsa.edocumentation.service.mapping.*;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
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

  AddressMapper addressMapper =
    Mappers.getMapper(AddressMapper.class);
  LocationMapper locationMapper = Mappers.getMapper(LocationMapper.class);

  @Mock ConsignmentItemMapper consignmentItemMapper;

  @Mock DisplayedAddressMapper displayedAddressMapper;

  @Spy
  TransportDocumentMapper transportDocumentMapper =
      Mappers.getMapper(TransportDocumentMapper.class);

  @InjectMocks TransportDocumentService transportDocumentService;

  @BeforeEach
  void init() {
    ReflectionTestUtils.setField(locationMapper, "addressMapper", addressMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "addressMapper", addressMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "locationMapper", locationMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "locMapper", locationMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "consignmentItemMapper", consignmentItemMapper);
    ReflectionTestUtils.setField(transportDocumentMapper, "displayedAddressMapper", displayedAddressMapper);
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
