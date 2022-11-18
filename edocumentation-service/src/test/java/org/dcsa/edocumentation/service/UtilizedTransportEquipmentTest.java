package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.UtilizedTransportEquipmentEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.repository.UtilizedTransportEquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UtilizedTransportEquipmentTest {
  @Mock UtilizedTransportEquipmentRepository utilizedTransportEquipmentRepository;
  @Mock EquipmentService equipmentService;

  @Spy
  UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper =
      Mappers.getMapper(UtilizedTransportEquipmentMapper.class);

  @Spy EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);

  @InjectMocks UtilizedTransportEquipmentService utilizedTransportEquipmentService;

  @Captor ArgumentCaptor<Collection<UtilizedTransportEquipmentTO>> utilizedTransportEquipmentsToCapture;

  @Test
  void utilizedTransportEquipmentTest_oneShipperOwnedEquipment() {
    UtilizedTransportEquipmentTO utilizedTransportEquipmentTO =
        UtilizedTransportEquipmentEquipmentDataFactory.singleShipperOwned();

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        List.of(utilizedTransportEquipmentTO));
    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      List.of(utilizedTransportEquipmentTO),
        utilizedTransportEquipmentsToCapture.getValue());
  }

  @Test
  void utilizedTransportEquipmentTest_twoShipperOwnedEquipments() {

    List<UtilizedTransportEquipmentTO> mockUtilizedTransportEquipments =
        UtilizedTransportEquipmentEquipmentDataFactory.multipleShipperOwned();

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

    Set<String> mockCarrierOwnedEquipmentReferences =
        mockUtilizedTransportEquipments.stream()
            .filter(Predicate.not(UtilizedTransportEquipmentTO::isShipperOwned))
            .map(UtilizedTransportEquipmentTO::equipment)
            .map(EquipmentTO::equipmentReference)
            .collect(Collectors.toSet());

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

    Set<String> mockCarrierOwnedEquipmentReferences =
        mockUtilizedTransportEquipments.stream()
            .filter(Predicate.not(UtilizedTransportEquipmentTO::isShipperOwned))
            .map(UtilizedTransportEquipmentTO::equipment)
            .map(EquipmentTO::equipmentReference)
            .collect(Collectors.toSet());

    utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        mockUtilizedTransportEquipments);
    verify(equipmentService, times(1)).resolveEquipments(utilizedTransportEquipmentsToCapture.capture(), any(), any());
    assertEquals(
      mockUtilizedTransportEquipments,
        utilizedTransportEquipmentsToCapture.getValue());
  }
}
