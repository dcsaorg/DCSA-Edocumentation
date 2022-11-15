package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.EquipmentDataFactory;
import org.dcsa.edocumentation.datafactories.RequestedEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.repository.EquipmentRepository;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.transferobjects.EquipmentTO;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import static org.junit.jupiter.api.Assertions.*;

import org.dcsa.skernel.errors.exceptions.BadRequestException;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceTest {
  @Mock private EquipmentRepository equipmentRepository;
  @Spy private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);

  @InjectMocks private EquipmentService equipmentService;

  @BeforeEach
  public void resetMocks() {
    reset(equipmentRepository);
  }

  @Test
  public void testNull() {
    assertTrue(equipmentService.resolveEquipments(null, null, null).isEmpty());

    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(equipmentRepository, never()).save(any());
    verify(equipmentMapper, never()).toDAO(any());
  }

  @Test
  public void testEmpty() {
    assertTrue(equipmentService.resolveEquipments(Collections.emptyList(), null, null).isEmpty());

    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(equipmentRepository, never()).save(any());
    verify(equipmentMapper, never()).toDAO(any());
  }

  @Test
  public void testCreate_AllFound() {
    // Setup
    List<RequestedEquipmentTO> requestedEquipmentTOS = RequestedEquipmentDataFactory.requestedEquipmentTOList();
    List<Equipment> equipments = EquipmentDataFactory.equipmentList();
    when(equipmentRepository.findByEquipmentReferences(any())).thenReturn(equipments);

    // Execute
    Map<String, Equipment> actual = equipmentService.resolveEquipments(requestedEquipmentTOS, RequestedEquipmentTO::isShipperOwned,
      re -> equipmentMapper.toNonNullableDTOStream(re.equipmentReferences()));

    // Verify
    assertEquals(EquipmentDataFactory.equipmentMap(), actual);
    verify(equipmentRepository).findByEquipmentReferences(EquipmentDataFactory.equipmentReferenceList());
    verify(equipmentRepository, never()).save(any());
  }

  @Test
  public void testCreate_CreateOne() {
    // Setup
    RequestedEquipmentTO extraRequestedEquipmentTO = RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("notExist"))
      .sizeType("size Type 2")
      .units(1)
      .isShipperOwned(true)
      .build();
    Equipment extraEquipment = Equipment.builder().equipmentReference("notExist").build();

    List<RequestedEquipmentTO> requestedEquipmentTOS =
      listPlusMore(RequestedEquipmentDataFactory.requestedEquipmentTOList(), extraRequestedEquipmentTO);

    Map<String, Equipment> expected = EquipmentDataFactory.equipmentMap();
    expected.put(extraEquipment.getEquipmentReference(), extraEquipment);

    when(equipmentRepository.findByEquipmentReferences(any())).thenReturn(EquipmentDataFactory.equipmentList());
    when(equipmentRepository.save(any())).thenReturn(extraEquipment);

    // Execute
    Map<String, Equipment> actual = equipmentService.resolveEquipments(requestedEquipmentTOS, RequestedEquipmentTO::isShipperOwned,
      re -> equipmentMapper.toNonNullableDTOStream(re.equipmentReferences()));

    // Verify
    assertEquals(expected, actual);
    verify(equipmentRepository).findByEquipmentReferences(setPlusMore(EquipmentDataFactory.equipmentReferenceList(), "notExist"));
    verify(equipmentRepository).save(extraEquipment);
  }

  @Test
  public void testCreate_NotFound() {
    // Setup
    RequestedEquipmentTO extraRequestedEquipmentTO = RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("notExist"))
      .sizeType("size Type 2")
      .units(1)
      .isShipperOwned(false)
      .build();

    List<RequestedEquipmentTO> requestedEquipmentTOS =
      listPlusMore(RequestedEquipmentDataFactory.requestedEquipmentTOList(), extraRequestedEquipmentTO);

    when(equipmentRepository.findByEquipmentReferences(any())).thenReturn(EquipmentDataFactory.equipmentList());

    // Execute
    NotFoundException exception = assertThrows(NotFoundException.class, () ->
      equipmentService.resolveEquipments(requestedEquipmentTOS, RequestedEquipmentTO::isShipperOwned,
      re -> equipmentMapper.toNonNullableDTOStream(re.equipmentReferences())));

    // Verify
    assertEquals("Could not find the following equipmentReferences in equipments: [notExist]", exception.getMessage());
    verify(equipmentRepository).findByEquipmentReferences(setPlusMore(EquipmentDataFactory.equipmentReferenceList(), "notExist"));
    verify(equipmentRepository, never()).save(any());
  }

  @Test
  public void testCreate_OverlappingReferences() {
    // Setup
    RequestedEquipmentTO extraRequestedEquipmentTO = RequestedEquipmentTO.builder()
      .equipmentReferences(List.of("Equipment_Ref_01"))
      .sizeType("size Type 2")
      .units(1)
      .isShipperOwned(false)
      .build();

    List<RequestedEquipmentTO> requestedEquipmentTOS =
      listPlusMore(RequestedEquipmentDataFactory.requestedEquipmentTOList(), extraRequestedEquipmentTO);

    // Execute
    BadRequestException exception = assertThrows(BadRequestException.class, () ->
      equipmentService.resolveEquipments(requestedEquipmentTOS, RequestedEquipmentTO::isShipperOwned,
        re -> equipmentMapper.toNonNullableDTOStream(re.equipmentReferences())));

    // Verify
    assertEquals("equipmentReference = 'Equipment_Ref_01' is used more than once in List<RequestedEquipmentTO>", exception.getMessage());
    verify(equipmentRepository, never()).findByEquipmentReferences(any());
    verify(equipmentRepository, never()).save(any());
  }

  private <T> List<T> listPlusMore(List<T> list, T more) {
    List<T> result = new ArrayList<>(list.size() + 1);
    result.addAll(list);
    result.add(more);
    return result;
  }

  private <T> Set<T> setPlusMore(Set<T> set, T more) {
    Set<T> result = new HashSet<>(set);
    result.add(more);
    return result;
  }
}
