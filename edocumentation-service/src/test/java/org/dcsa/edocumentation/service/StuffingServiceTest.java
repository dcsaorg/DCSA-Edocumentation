package org.dcsa.edocumentation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.dcsa.edocumentation.datafactories.ConsignmentItemDataFactory;
import org.dcsa.edocumentation.datafactories.ConfirmedBookingDataFactory;
import org.dcsa.edocumentation.datafactories.ShippingInstructionDataFactory;
import org.dcsa.edocumentation.datafactories.UtilizedTransportEquipmentEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ConfirmedBookingRepository;
import org.dcsa.edocumentation.service.mapping.CargoItemMapper;
import org.dcsa.edocumentation.service.mapping.ConsignmentItemMapper;
import org.dcsa.edocumentation.service.mapping.EquipmentMapper;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class StuffingServiceTest {
  @Mock private ConfirmedBookingRepository confirmedBookingRepository;
  @Mock private ConsignementItemRepository consignementItemRepository;


  @Spy private CargoItemMapper cargoItemMapper = Mappers.getMapper(CargoItemMapper.class);
  @Spy
  private ConsignmentItemMapper consignmentItemMapper =
      Mappers.getMapper(ConsignmentItemMapper.class);
  @Spy private EquipmentMapper equipmentMapper = Mappers.getMapper(EquipmentMapper.class);

  @InjectMocks private StuffingService stuffingService;

  @Captor ArgumentCaptor<List<ConsignmentItem>> savedConsignmentItems;

  private UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper =
      Mappers.getMapper(UtilizedTransportEquipmentMapper.class);

  private ShippingInstruction shippingInstruction;
  private Map<String, UtilizedTransportEquipment> savedUtilizedTransportEquipments;
  private List<ConsignmentItemTO> consignmentItemTOs;
  private ConfirmedBooking confirmedBooking;

  @BeforeEach
  void init() {
    ReflectionTestUtils.setField(utilizedTransportEquipmentMapper, "equipmentMapper", equipmentMapper);
    ReflectionTestUtils.setField(consignmentItemMapper, "cargoItemMapper", cargoItemMapper);
    shippingInstruction = ShippingInstructionDataFactory.singleShallowShippingInstruction();
    savedUtilizedTransportEquipments =
        UtilizedTransportEquipmentEquipmentDataFactory.multipleCarrierOwned().stream()
            .map(ute -> utilizedTransportEquipmentMapper.toDAO(ute, Equipment.builder().equipmentReference(ute.equipmentReference()).build()))
            .collect(
                Collectors.toMap(
                    utilizedTransportEquipment -> utilizedTransportEquipment.getEquipment().getEquipmentReference(),
                    Function.identity()));

    consignmentItemTOs = List.of(ConsignmentItemDataFactory.singleConsignmentItem());
    confirmedBooking = ConfirmedBookingDataFactory.singleConfirmedBookingWithBooking();
  }

  @Test
  void stuffingServiceTest_testSavedConsignmentItem() {
    when(confirmedBookingRepository.findByCarrierBookingReference("CBR_01"))
        .thenReturn(Optional.of(confirmedBooking));

    stuffingService.createStuffing(
        shippingInstruction, savedUtilizedTransportEquipments, consignmentItemTOs);

    verify(consignementItemRepository).saveAll(savedConsignmentItems.capture());

    ConsignmentItem savedConsignmentItem = savedConsignmentItems.getValue().get(0);
    List<CargoItem> savedCargoItems = savedConsignmentItem.getCargoItems().stream().toList();

    assertEquals(1, savedConsignmentItems.getValue().size());
    assertEquals(
        shippingInstruction.getShippingInstructionReference(),
        savedConsignmentItem.getShippingInstruction().getShippingInstructionReference());

    assertEquals(
        confirmedBooking.getCarrierBookingReference(),
        savedConsignmentItem.getConfirmedBooking().getCarrierBookingReference());

    assertEquals(2, savedCargoItems.size());
    assertNotNull(
        savedUtilizedTransportEquipments.get(
            savedCargoItems
                .get(0)
                .getUtilizedTransportEquipment()
                .getEquipment()
                .getEquipmentReference()));

    assertEquals(
        2,
        savedCargoItems.stream()
            .map(CargoItem::getShippingMarks)
            .filter(Objects::nonNull)
            .mapToInt(List::size)
            .sum());
  }

  @Test
  void stuffingServiceTest_testCargoItemDoesNotReferToExistingShipment() {
    when(confirmedBookingRepository.findByCarrierBookingReference("CBR_01")).thenReturn(Optional.empty());

    ConcreteRequestErrorMessageException exceptionCaught =
        assertThrows(
            ConcreteRequestErrorMessageException.class,
            () ->
                stuffingService.createStuffing(
                    shippingInstruction, savedUtilizedTransportEquipments, consignmentItemTOs));

    assertEquals(
        "No confirmedBooking has been found for this carrierBookingReference: CBR_01",
        exceptionCaught.getMessage());
  }

  @Test
  void stuffingServiceTest_testNoUtilizedTransportEquipmentMatch() {
    when(confirmedBookingRepository.findByCarrierBookingReference("CBR_01"))
      .thenReturn(Optional.of(confirmedBooking));

    savedUtilizedTransportEquipments = UtilizedTransportEquipmentEquipmentDataFactory.multipleShipperOwned().stream()
      .map(ute -> utilizedTransportEquipmentMapper.toDAO(ute, equipmentMapper.toDAO(ute.equipment())))
      .collect(
        Collectors.toMap(
          utilizedTransportEquipment -> utilizedTransportEquipment.getEquipment().getEquipmentReference(),
          Function.identity()));

    ConcreteRequestErrorMessageException exceptionCaught =
      assertThrows(
        ConcreteRequestErrorMessageException.class,
        () ->
          stuffingService.createStuffing(
            shippingInstruction, savedUtilizedTransportEquipments, consignmentItemTOs));

    assertEquals(
      "Could not find utilizedTransportEquipments for this cargoItems, based on equipmentReference: CARR_EQ_REF_01",
      exceptionCaught.getMessage());
  }
}
