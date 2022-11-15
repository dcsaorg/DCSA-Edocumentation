package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.EquipmentDataFactory;
import org.dcsa.edocumentation.datafactories.RequestedEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestedEquipmentServiceTest {
  @Mock private EquipmentService equipmentService;
  @Mock private RequestedEquipmentRepository requestedEquipmentRepository;
  @Spy private RequestedEquipmentMapper requestedEquipmentMapper = Mappers.getMapper(RequestedEquipmentMapper.class);
  @Spy private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);


  @InjectMocks private RequestedEquipmentService requestedEquipmentService;

  @BeforeEach
  public void resetMocks() {
    reset(equipmentService, requestedEquipmentRepository);
  }

  @Test
  public void testCreateNull() {
    requestedEquipmentService.createRequestedEquipments(null, null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentRepository, never()).saveAll(any());
    verify(requestedEquipmentMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreateEmpty() {
    requestedEquipmentService.createRequestedEquipments(Collections.emptyList(), null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentRepository, never()).saveAll(any());
    verify(requestedEquipmentMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreate_WithEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList();
    when(equipmentService.resolveEquipments(any(), any(), any())).thenReturn(EquipmentDataFactory.equipmentMap());

    // Execute
    requestedEquipmentService.createRequestedEquipments(requestedEquipmentTOS, booking);

    // Verify
    verify(equipmentService).resolveEquipments(eq(requestedEquipmentTOS), any(), any());
    verify(requestedEquipmentRepository).saveAll(RequestedEquipmentDataFactory.requestedEquipmentList());
  }
}
