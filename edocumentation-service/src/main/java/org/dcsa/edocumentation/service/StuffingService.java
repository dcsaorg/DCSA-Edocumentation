package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.ConsignementItemRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.ConsignmentItemMapper;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StuffingService {

  private final ShipmentRepository shipmentRepository;
  private final ConsignmentItemMapper consignmentItemMapper;
  private final ConsignementItemRepository consignementItemRepository;

  public void createStuffing(
      ShippingInstruction shippingInstruction,
      List<UtilizedTransportEquipment> savedTransportEquipments,
      List<ConsignmentItemTO> consignmentItemTOs) {

    // ToDo can be moved to UtilizedTransportEquipment Service as return value
    Map<String, UtilizedTransportEquipment> uteByEquipmentReference =
        savedTransportEquipments.stream()
            .collect(
                Collectors.toMap(
                    utilizedTransportEquipment ->
                        utilizedTransportEquipment.getEquipment().getEquipmentReference(),
                    utilizedTransportEquipment -> utilizedTransportEquipment));

    List<ConsignmentItem> consignmentItemsWithShipmentsCargoItemsAndUtilizedTransportEquipments =
        consignmentItemTOs.stream()
            .map(
                consignmentItemTO -> {
                  ConsignmentItem consignmentItem = consignmentItemMapper.toDAO(consignmentItemTO);
                  return shipmentRepository
                      .findByCarrierBookingReference(consignmentItemTO.carrierBookingReference())
                      .map(
                          shipment ->
                              consignmentItem.toBuilder()
                                  .shipment(shipment)
                                  .shippingInstruction(shippingInstruction)
                                  .cargoItems(
                                      consignmentItemTO.cargoItems().stream()
                                          .flatMap(
                                              cargoItemTO -> {
                                                String equipmentReference =
                                                    cargoItemTO.equipmentReference();
                                                UtilizedTransportEquipment ute =
                                                    uteByEquipmentReference.get(equipmentReference);
                                                return consignmentItem.getCargoItems().stream()
                                                    .map(
                                                        cargoItem ->
                                                            cargoItem.toBuilder()
                                                                .utilizedTransportEquipment(ute)
                                                                .build());
                                              })
                                          .collect(Collectors.toSet())))
                      .orElseThrow(
                          () ->
                              ConcreteRequestErrorMessageException.notFound(
                                  "No shipment has been found for this carrierBookingReference: "
                                      + consignmentItemTO.carrierBookingReference()));
                })
            .map(ConsignmentItem.ConsignmentItemBuilder::build)
            .toList();

    consignementItemRepository.saveAll(
        consignmentItemsWithShipmentsCargoItemsAndUtilizedTransportEquipments);
  }
}
