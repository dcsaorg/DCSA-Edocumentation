package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.ConsignmentItemMapper;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StuffingService {

  private final ShipmentRepository shipmentRepository;
  private final ConsignmentItemMapper consignmentItemMapper;
  private final ConsignementItemRepository consignementItemRepository;

  public void createStuffing(
      ShippingInstruction shippingInstruction,
      Map<String, UtilizedTransportEquipment> savedTransportEquipments,
      List<ConsignmentItemTO> consignmentItemTOs) {

    List<ConsignmentItem> consignmentItems =
        consignmentItemTOs.stream()
            .map(
                consignmentItemTO -> {
                  ConsignmentItem consignmentItem = consignmentItemMapper.toDAO(consignmentItemTO);
                  return consignmentItem.toBuilder()
                      .shipment(addShipment(consignmentItemTO.carrierBookingReference()))
                      .shippingInstruction(shippingInstruction)
                      .cargoItems(
                          addCargoItems(
                              consignmentItem,
                              consignmentItemTO.cargoItems(),
                              savedTransportEquipments))
                      .build();
                })
            .toList();

    consignementItemRepository.saveAll(consignmentItems);
  }

  // A Shipping instruction must link to a shipment (=approved booking)
  private Shipment addShipment(String carrierBookingReference) {
    return shipmentRepository
        .findByCarrierBookingReference(carrierBookingReference)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No shipment has been found for this carrierBookingReference: "
                        + carrierBookingReference));
  }

  // Cargo items act as 'join-table' for utilizedTransportEquipment and consignment Items
  private Set<CargoItem> addCargoItems(
      ConsignmentItem consignmentItem,
      List<CargoItemTO> cargoItems,
      Map<String, UtilizedTransportEquipment> savedTransportEquipments) {
    return cargoItems.stream()
        .flatMap(
            cargoItemTO -> {
              String equipmentReference = cargoItemTO.equipmentReference();
              UtilizedTransportEquipment ute = savedTransportEquipments.get(equipmentReference);
              return consignmentItem.getCargoItems().stream()
                  .map(cargoItem -> cargoItem.toBuilder().utilizedTransportEquipment(ute).build());
            })
        .collect(Collectors.toSet());
  }
}
