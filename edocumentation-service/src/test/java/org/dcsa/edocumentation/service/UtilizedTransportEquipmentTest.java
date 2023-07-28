package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.EquipmentDataFactory;
import org.dcsa.edocumentation.datafactories.UtilizedTransportEquipmentEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.SealMapper;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UtilizedTransportEquipmentTest {
  @Mock UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;
  @Mock EquipmentService equipmentService;

  @Spy
  UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper =
      Mappers.getMapper(UtilizedTransportEquipmentMapper.class);

  @Spy EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);
  @Spy SealMapper sealMapper = Mappers.getMapper(SealMapper.class);

  @InjectMocks UtilizedTransportEquipmentService utilizedTransportEquipmentService;

  @Captor ArgumentCaptor<Collection<UtilizedTransportEquipmentTO>> utilizedTransportEquipmentsToCapture;

  @BeforeEach
  void setup() {

    ReflectionTestUtils.setField(utilizedTransportEquipmentMapper, "equipmentMapper", equipmentMapper);
    ReflectionTestUtils.setField(utilizedTransportEquipmentMapper, "sealMapper", sealMapper);
  }

  @Test
  void utilizedTransportEquipmentTest_oneShipperOwnedEquipment() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO =
        UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwned();

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(EquipmentDataFactory.equipmentMap());
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    // Execute
    utilizedTransportEquipmentService.createUtilizedTransportEquipment(List.of(utilizedTransportEquipmentTO));

    // Verify
    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      List.of(utilizedTransportEquipmentTO),
        utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_twoShipperOwnedEquipments() {

    List<UtilizedTransportEquipmentTO> mockUtilizedTransportEquipments =
        UtilizedTransportEquipmentEquipmentDataFactory.multipleShipperOwned();

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(EquipmentDataFactory.equipmentMap());
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        mockUtilizedTransportEquipments);

    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      mockUtilizedTransportEquipments,
        utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_oneCarrierOwnedEquipment() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO =
        UtilizedTransportEquipmentEquipmentDataFactory.singleCarrierOwned();

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(EquipmentDataFactory.equipmentMap());
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        List.of(utilizedTransportEquipmentTO));

    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
        List.of(utilizedTransportEquipmentTO),
        utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_oneCarrierOwnedEquipmentWithSeal() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO =
      UtilizedTransportEquipmentEquipmentDataFactory.singleCarrierOwnedWithSeal();

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(EquipmentDataFactory.equipmentMap());
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
      List.of(utilizedTransportEquipmentTO));

    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      List.of(utilizedTransportEquipmentTO),
      utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_twoCarrierOwnedEquipment() {
    List<UtilizedTransportEquipmentTO> mockUtilizedTransportEquipments =
        UtilizedTransportEquipmentEquipmentDataFactory.multipleCarrierOwned();

    Map<String, Equipment> mockCarrierOwnedEquipmentReferences =
        mockUtilizedTransportEquipments.stream()
          .map(UtilizedTransportEquipmentTO::equipmentReference)
          .collect(Collectors.toMap(ref -> ref, ref -> Equipment.builder().equipmentReference(ref).build()));

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(mockCarrierOwnedEquipmentReferences);
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        mockUtilizedTransportEquipments);

    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      mockUtilizedTransportEquipments,
        utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_bothCarrierOwnedAndShipperEquipment() {
    List<UtilizedTransportEquipmentTO> mockUtilizedTransportEquipments =
        List.of(
            UtilizedTransportEquipmentEquipmentDataFactory.singleCarrierOwned(),
            UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwned());

    Map<String, Equipment> mockCarrierOwnedEquipmentReferences =
        mockUtilizedTransportEquipments.stream()
            .filter(Predicate.not(UtilizedTransportEquipmentTO::isShipperOwned))
            .map(UtilizedTransportEquipmentTO::equipment)
            .collect(Collectors.toMap(EquipmentTO::equipmentReference, equipmentMapper::toDAO));

    when(equipmentService.resolveEquipments(any(), any(), any()))
      .thenReturn(mockCarrierOwnedEquipmentReferences);
    when(utilizedTransportEquipmentRepository.save(any()))
      .thenReturn(UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwnedDao());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        mockUtilizedTransportEquipments);
    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      mockUtilizedTransportEquipments,
        utilizedTransportEquipmentsToCapture.getValue());
  }
}
