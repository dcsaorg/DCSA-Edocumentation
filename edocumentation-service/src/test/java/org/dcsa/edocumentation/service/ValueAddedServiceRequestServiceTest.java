package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.ValueAddedServiceRequestDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedService;
import org.dcsa.edocumentation.domain.persistence.repository.ValueAddedServiceRequestRepository;
import org.dcsa.edocumentation.service.mapping.ValueAddedServiceRequestMapper;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValueAddedServiceRequestServiceTest {
  @Mock private ValueAddedServiceRequestRepository valueAddedServiceRequestRepository;
  @Spy private ValueAddedServiceRequestMapper valueAddedServiceRequestMapper =
    Mappers.getMapper(ValueAddedServiceRequestMapper.class);

  @InjectMocks
  private ValueAddedServiceRequestService valueAddedServiceRequestService;

  @BeforeEach
  public void resetMocks() {
    reset(valueAddedServiceRequestRepository);
  }

  @Test
  public void testCreateNull() {
    valueAddedServiceRequestService.createValueAddedServiceRequests(null, null);

    verify(valueAddedServiceRequestRepository, never()).saveAll(any());
    verify(valueAddedServiceRequestMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreateEmpty() {
    valueAddedServiceRequestService.createValueAddedServiceRequests(Collections.emptyList(), null);

    verify(valueAddedServiceRequestRepository, never()).saveAll(any());
    verify(valueAddedServiceRequestMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreate() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    ValueAddedServiceTO ValueAddedServiceTO = ValueAddedServiceRequestDataFactory.singleValueAddedServiceRequestTO();
    ValueAddedService ValueAddedService = ValueAddedServiceRequestDataFactory.singleValueAddedServiceRequestWithoutId();

    when(valueAddedServiceRequestMapper.toDAO(any(ValueAddedServiceTO.class), any(Booking.class))).thenReturn(ValueAddedService);

    // Execute
    valueAddedServiceRequestService.createValueAddedServiceRequests(List.of(ValueAddedServiceTO), booking);

    // Verify
    verify(valueAddedServiceRequestRepository).saveAll(List.of(ValueAddedService));
  }
}
