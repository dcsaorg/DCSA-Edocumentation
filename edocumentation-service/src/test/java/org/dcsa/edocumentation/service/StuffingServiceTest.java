package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ConsignmentItemDataFactory;
import org.dcsa.edocumentation.datafactories.ShipmentDataFactory;
import org.dcsa.edocumentation.datafactories.ShippingInstructionDataFactory;
import org.dcsa.edocumentation.datafactories.UtilizedTransportEquipmentEquipmentDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.CargoItemMapper;
import org.dcsa.edocumentation.service.mapping.ConsignmentItemMapper;
import org.dcsa.edocumentation.service.mapping.UtilizedTransportEquipmentMapper;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StuffingServiceTest {
  @Mock private ShipmentRepository shipmentRepository;
  @Mock private ConsignementItemRepository consignementItemRepository;

  @Spy
  private ConsignmentItemMapper consignmentItemMapper =
      Mappers.getMapper(ConsignmentItemMapper.class);

  @Spy private CargoItemMapper cargoItemMapper = Mappers.getMapper(CargoItemMapper.class);

  @InjectMocks private StuffingService stuffingService;

  @Captor ArgumentCaptor<List<ConsignmentItem>> savedConsignmentItems;

  private UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper =
      Mappers.getMapper(UtilizedTransportEquipmentMapper.class);

  private ShippingInstruction shippingInstruction;
  private Map<String, UtilizedTransportEquipment> savedUtilizedTransportEquipments;
  private List<ConsignmentItemTO> consignmentItemTOs;
  private Shipment shipment;

  @BeforeEach
  void init() {
    shippingInstruction = ShippingInstructionDataFactory.singleShallowShippingInstruction();
    savedUtilizedTransportEquipments =
        UtilizedTransportEquipmentEquipmentDataFactory.multipleCarrierOwned().stream()
            .map(utilizedTransportEquipmentMapper::toDAO)
            .collect(
                Collectors.toMap(
                    utilizedTransportEquipment ->
                        utilizedTransportEquipment.getEquipment().getEquipmentReference(),
                    utilizedTransportEquipment -> utilizedTransportEquipment));

    consignmentItemTOs = List.of(ConsignmentItemDataFactory.singleConsignmentItem());
    shipment = ShipmentDataFactory.singleShipmentWithBooking();
  }

  @Test
  void stuffingServiceTest_testSavedConsignmentItem() {
    when(shipmentRepository.findByCarrierBookingReference("CBR_01"))
        .thenReturn(Optional.of(shipment));

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
        shipment.getCarrierBookingReference(),
        savedConsignmentItem.getShipment().getCarrierBookingReference());

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
            .map(CargoItem::getCargoLineItems)
            .filter(Objects::nonNull)
            .mapToInt(Set::size)
            .sum());
  }

  @Test
  void stuffingServiceTest_testCargoItemDoesNotReferToExistingShipment() {
    when(shipmentRepository.findByCarrierBookingReference("CBR_01")).thenReturn(Optional.empty());

    ConcreteRequestErrorMessageException exceptionCaught =
        assertThrows(
            ConcreteRequestErrorMessageException.class,
            () ->
                stuffingService.createStuffing(
                    shippingInstruction, savedUtilizedTransportEquipments, consignmentItemTOs));

    assertEquals(
        "No shipment has been found for this carrierBookingReference: CBR_01",
        exceptionCaught.getMessage());
  }

  @Test
  void stuffingServiceTest_testNoUtilizedTransportEquipmentMatch() {
    when(shipmentRepository.findByCarrierBookingReference("CBR_01"))
      .thenReturn(Optional.of(shipment));

    savedUtilizedTransportEquipments = UtilizedTransportEquipmentEquipmentDataFactory.multipleShipperOwned().stream()
      .map(utilizedTransportEquipmentMapper::toDAO)
      .collect(
        Collectors.toMap(
          utilizedTransportEquipment ->
            utilizedTransportEquipment.getEquipment().getEquipmentReference(),
          utilizedTransportEquipment -> utilizedTransportEquipment));

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
