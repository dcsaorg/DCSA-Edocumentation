package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RequestedEquipmentGroupServiceTest {
  @Mock private EquipmentService equipmentService;
  @Mock private RequestedEquipmentGroupRepository requestedEquipmentGroupRepository;
  @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
  @Spy private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);


  @InjectMocks private RequestedEquipmentGroupService requestedEquipmentGroupService;

  @BeforeEach
  public void resetMocks() {
    reset(equipmentService, requestedEquipmentGroupRepository);
  }

  @Test
  public void testCreateNull() {
    requestedEquipmentGroupService.createRequestedEquipments(null, null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentGroupRepository, never()).saveAll(any());
    verify(requestedEquipmentGroupMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreateEmpty() {
    requestedEquipmentGroupService.createRequestedEquipments(Collections.emptyList(), null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentGroupRepository, never()).saveAll(any());
    verify(requestedEquipmentGroupMapper, never()).toDAO(any(), any());
  }

  /* TODO fix this
  @Test
  public void testCreate_WithEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList();
    when(equipmentService.resolveEquipments(any(), any(), any())).thenReturn(EquipmentDataFactory.equipmentMap());

    // Execute
    requestedEquipmentGroupService.createRequestedEquipments(requestedEquipmentTOS, booking);

    // Verify
    verify(equipmentService).resolveEquipments(eq(requestedEquipmentTOS), any(), any());
    // verify(requestedEquipmentGroupRepository).saveAll(RequestedEquipmentDataFactory.requestedEquipmentList());
  }
   */
}
