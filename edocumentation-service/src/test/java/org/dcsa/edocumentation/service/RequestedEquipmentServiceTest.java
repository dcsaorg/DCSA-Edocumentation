package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingDataFactory;
import org.dcsa.edocumentation.datafactories.RequestedEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.RequestedEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.RequestedEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import static org.junit.jupiter.api.Assertions.*;
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
public class RequestedEquipmentServiceTest {
  @Mock private EquipmentRepository equipmentRepository;
  @Mock private RequestedEquipmentRepository requestedEquipmentRepository;
  @Spy private RequestedEquipmentMapper requestedEquipmentMapper = Mappers.getMapper(RequestedEquipmentMapper.class);

  @InjectMocks private RequestedEquipmentService requestedEquipmentService;

  @BeforeEach
  public void resetMocks() {
    reset(equipmentRepository, requestedEquipmentRepository);
  }

  @Test
  public void testCreateNull() {
    requestedEquipmentService.createRequestedEquipments(null, null);

    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(requestedEquipmentRepository, never()).saveAll(any());
    verify(requestedEquipmentMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreateEmpty() {
    requestedEquipmentService.createRequestedEquipments(Collections.emptyList(), null);

    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(requestedEquipmentRepository, never()).saveAll(any());
    verify(requestedEquipmentMapper, never()).toDAO(any(), any());
  }

  @Test
  public void testCreate_NoEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList().stream()
      .map(re -> re.toBuilder().equipmentReferences(null).build())
      .toList();

    // Execute
    requestedEquipmentService.createRequestedEquipments(requestedEquipmentTOS, booking);

    // Verify
    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(requestedEquipmentRepository).saveAll(
      RequestedEquipmentDataFactory.requestedEquipmentList().stream()
        .map(re -> re.toBuilder().equipments(null).build())
        .toList()
    );
  }

  @Test
  public void testCreate_WithEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList();
    when(equipmentRepository.findByEquipmentReferences(any())).thenReturn(RequestedEquipmentDataFactory.equipmentList());

    // Execute
    requestedEquipmentService.createRequestedEquipments(requestedEquipmentTOS, booking);

    // Verify
    verify(equipmentRepository).findByEquipmentReferences(RequestedEquipmentDataFactory.equipmentReferenceList());
    verify(requestedEquipmentRepository).saveAll(RequestedEquipmentDataFactory.requestedEquipmentList());
  }

  @Test
  public void testCreate_WithUnknownEquipmentReferences() {
    // Setup
    Booking booking = BookingDataFactory.singleMinimalBooking();
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList();
    when(equipmentRepository.findByEquipmentReferences(any())).thenReturn(
      RequestedEquipmentDataFactory.equipmentList().stream()
        .filter(re -> !re.getEquipmentReference().equals("Equipment_Ref_01"))
        .toList()
    );

    // Execute
    NotFoundException exception = assertThrows(NotFoundException.class, () ->
      requestedEquipmentService.createRequestedEquipments(requestedEquipmentTOS, booking)
    );

    // Verify
    assertEquals("Could not find the following equipmentReferences in equipments: [Equipment_Ref_01]", exception.getMessage());
    verify(equipmentRepository).findByEquipmentReferences(RequestedEquipmentDataFactory.equipmentReferenceList());
    verify(requestedEquipmentRepository, never()).saveAll(any());
  }
}
