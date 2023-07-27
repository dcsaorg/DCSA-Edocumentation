package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.domain.persistence.repository.CommodityRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.CargoItemMapper;
import org.dcsa.edocumentation.service.mapping.ConsignmentItemMapper;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StuffingService {

  private final ShipmentRepository shipmentRepository;
  private final ConsignmentItemMapper consignmentItemMapper;
  private final ConsignementItemRepository consignementItemRepository;
  private final CargoItemMapper cargoItemMapper;
  private final CommodityRepository commodityRepository;

  @Transactional(Transactional.TxType.MANDATORY)
  public void createStuffing(
      ShippingInstruction shippingInstruction,
      Map<String, UtilizedTransportEquipment> savedTransportEquipments,
      List<ConsignmentItemTO> consignmentItemTOs) {

    List<ConsignmentItem> consignmentItems =
        consignmentItemTOs.stream()
            .map(
                consignmentItemTO -> {
                  ConsignmentItem consignmentItem = consignmentItemMapper.toDAO(consignmentItemTO);
                  Shipment shipment = addShipment(consignmentItemTO.carrierBookingReference());
                  Commodity commodity = commodityRepository.save(Commodity.builder()
                    .booking(shipment.getBooking())
                    .commodityType(consignmentItem.getDescriptionOfGoods())
                    .hsCodes(consignmentItemTO.hsCodes())
                    .build()
                  );
                  return consignmentItem.toBuilder()
                      .commodity(commodity)
                      .shipment(shipment)
                      .shippingInstruction(shippingInstruction)
                      .cargoItems(
                          addCargoItems(consignmentItemTO.cargoItems(), savedTransportEquipments))
                      .build();
                })
            .toList();

    consignementItemRepository.saveAll(consignmentItems);
  }

  // A Shipping instruction must link to a shipment (=approved booking) consignmentItems acts like a
  // 'join-table' between shipping instruction and shipment
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
  private List<CargoItem> addCargoItems(
      List<CargoItemTO> cargoItems,
      Map<String, UtilizedTransportEquipment> savedTransportEquipments) {

    return cargoItems.stream()
        .map(
            cargoItemTO ->
                cargoItemMapper.toDAO(cargoItemTO).toBuilder()
                    .utilizedTransportEquipment(
                        findSavedUtilizedTransportEquipmentViaCargoItem(
                            savedTransportEquipments, cargoItemTO))
                    .build())
        .toList();
  }

  private UtilizedTransportEquipment findSavedUtilizedTransportEquipmentViaCargoItem(
      Map<String, UtilizedTransportEquipment> savedTransportEquipments, CargoItemTO cargoItemTO) {
    UtilizedTransportEquipment ute = savedTransportEquipments.get(cargoItemTO.equipmentReference());
    if (ute == null) {
      throw ConcreteRequestErrorMessageException.invalidInput(
          "Could not find utilizedTransportEquipments for this cargoItems, based on equipmentReference: "
              + cargoItemTO.equipmentReference());
    }
    return ute;
  }
}
