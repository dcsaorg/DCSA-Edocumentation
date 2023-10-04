package org.dcsa.edocumentation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.CommodityDataFactory;
import org.dcsa.edocumentation.datafactories.EquipmentDataFactory;
import org.dcsa.edocumentation.datafactories.RequestedEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentGroupRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentGroupMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RequestedEquipmentGroupServiceTest {
  @Mock private EquipmentService equipmentService;
  @Mock private ActiveReeferSettingsService activeReeferSettingsService;
  @Mock private RequestedEquipmentGroupRepository requestedEquipmentGroupRepository;
  @Spy private RequestedEquipmentGroupMapper requestedEquipmentGroupMapper = Mappers.getMapper(RequestedEquipmentGroupMapper.class);
  @Spy private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);

  @InjectMocks private RequestedEquipmentGroupService requestedEquipmentGroupService;

  @BeforeEach
  public void resetMocks() {
    reset(equipmentService, activeReeferSettingsService, requestedEquipmentGroupRepository);
  }

  @Test
  public void testCreateNull() {
    requestedEquipmentGroupService.createRequestedEquipments(null, null, null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentGroupRepository, never()).saveAll(any());
    verify(requestedEquipmentGroupMapper, never()).toDAO(any(), any(), any(), any());
  }

  @Test
  public void testCreateEmpty() {
    requestedEquipmentGroupService.createRequestedEquipments(Collections.emptyList(), null, null);

    verify(equipmentService, never()).resolveEquipments(any(), any(), any());
    verify(requestedEquipmentGroupRepository, never()).saveAll(any());
    verify(requestedEquipmentGroupMapper, never()).toDAO(any(), any(), any(), any());
  }

  @Test
  public void testCreate_WithEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    Commodity commodity = CommodityDataFactory.singleCommodity();
    RequestedEquipmentTO requestedEquipmentTO = RequestedEquipmentDataFactory.requestedEquipmentTORef1();
    RequestedEquipmentGroup expectedGroup = RequestedEquipmentDataFactory.requestedEquipmentRef1();
    when(equipmentService.resolveEquipments(any(), any(), any())).thenReturn(EquipmentDataFactory.equipmentMap());
    when(requestedEquipmentGroupRepository.save(any())).thenReturn(RequestedEquipmentDataFactory.requestedEquipmentRef1());

    // Execute
    requestedEquipmentGroupService.createRequestedEquipments(List.of(requestedEquipmentTO), booking, commodity);

    // Verify
    verify(equipmentService).resolveEquipments(eq(List.of(requestedEquipmentTO)), any(), any());
    verify(requestedEquipmentGroupRepository).save(expectedGroup);
    verify(activeReeferSettingsService).createActiveReeferSettings(requestedEquipmentTO.activeReeferSettings());
  }
}
