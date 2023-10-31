package org.dcsa.edocumentation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.ReferenceDataFactory;
import org.dcsa.edocumentation.datafactories.ShippingInstructionDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.ReferenceRepository;
import org.dcsa.edocumentation.service.mapping.ReferenceMapper;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReferenceServiceTest {
  @Mock private ReferenceRepository referenceRepository;
  @Spy private ReferenceMapper referenceMapper = Mappers.getMapper(ReferenceMapper.class);

  @InjectMocks private ReferenceService referenceService;

  @BeforeEach
  public void resetMocks() {
    reset(referenceRepository);
  }

  @Test
  void referenceServiceTest_testCreateWithNullBooking() {
    referenceService.createReferences(null, (BookingData) null);

    verify(referenceRepository, never()).saveAll(any());
    verify(referenceMapper, never()).toDAO(any(), any(BookingData.class));
  }

  @Test
  void referenceServiceTest_testCreateWithNullShippingInstruction() {
    referenceService.createReferences(null, (ShippingInstruction) null);

    verify(referenceRepository, never()).saveAll(any());
    verify(referenceMapper, never()).toDAO(any(), any(ShippingInstruction.class));
  }

  @Test
  void referenceServiceTest_testCreateWithEmptyBooking() {
    referenceService.createReferences(Collections.emptyList(), (BookingData) null);

    verify(referenceRepository, never()).saveAll(any());
    verify(referenceMapper, never()).toDAO(any(), any(BookingData.class));
  }

  @Test
  void referenceServiceTest_testCreateWithEmptyShippingInstruction() {
    referenceService.createReferences(Collections.emptyList(), (ShippingInstruction) null);

    verify(referenceRepository, never()).saveAll(any());
    verify(referenceMapper, never()).toDAO(any(), any(ShippingInstruction.class));
  }

  @Test
  void referenceServiceTest_testCreateWithBooking() {
    // Setup
    var booking = BookingDataFactory.singleMinimalBooking();
    ReferenceTO referenceTO = ReferenceDataFactory.singleReferenceTO();
    Reference reference = ReferenceDataFactory.singleReferenceWithoutId();

    // Execute
    referenceService.createReferences(List.of(referenceTO), booking.getBookingData());

    // Verify
    verify(referenceMapper).toDAO(referenceTO, booking.getBookingData());
    verify(referenceRepository).saveAll(List.of(reference));
  }

  @Test
  void referenceServiceTest_testCreateWithShippingInstruction() {
    // Setup
    ShippingInstruction shippingInstruction =
        ShippingInstructionDataFactory.singleShallowShippingInstruction();
    ReferenceTO referenceTO = ReferenceDataFactory.singleReferenceTO();
    Reference reference =
        ReferenceDataFactory.singleReferenceWithoutId().toBuilder()
            .shippingInstructionID(shippingInstruction.getId())
            .build();

    // Execute
    referenceService.createReferences(List.of(referenceTO), shippingInstruction);

    // Verify
    verify(referenceMapper).toDAO(referenceTO, shippingInstruction);
    verify(referenceRepository).saveAll(List.of(reference));
  }
}
