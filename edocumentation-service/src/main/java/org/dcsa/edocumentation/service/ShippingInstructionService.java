package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.domain.persistence.repository.ShippingInstructionRepository;
import org.dcsa.edocumentation.service.mapping.ShippingInstructionMapper;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShippingInstructionService {

  private final ShippingInstructionRepository shippingInstructionRepository;
  private final ShippingInstructionMapper shippingInstructionMapper;
  private final DocumentPartyService documentPartyService;
  private final ReferenceService referenceService;
  private final UtilizedTransportEquipmentService utilizedTransportEquipmentService;
  private final ShipmentRepository shipmentRepository;

  @Transactional
  public Optional<ShippingInstructionTO> findByReference(String shippingInstructionReference) {
    return shippingInstructionRepository
        .findByShippingInstructionReferenceAndValidUntilIsNull(shippingInstructionReference)
        .map(shippingInstructionMapper::toDTO);
  }

  @Transactional
  public ShippingInstructionRefStatusTO createShippingInstruction(
      ShippingInstructionTO shippingInstructionTO) {

    ShippingInstruction shippingInstruction = shippingInstructionMapper.toDAO(shippingInstructionTO);
    // TODO: When finishing DT-578, this should not happen immediately on receiving a new shipping instruction
    Map<String, UtilizedTransportEquipment> savedUtilizedTransportEquipments =
      utilizedTransportEquipmentService.createUtilizedTransportEquipment(
        shippingInstructionTO.utilizedTransportEquipments());
    resolveStuffing(shippingInstruction, savedUtilizedTransportEquipments);
    shippingInstruction.receive();

    ShippingInstruction updatedShippingInstruction =
      shippingInstructionRepository.save(shippingInstruction);

    documentPartyService.createDocumentParties(
        shippingInstructionTO.documentParties(), updatedShippingInstruction);
    referenceService.createReferences(
        shippingInstructionTO.references(), updatedShippingInstruction);

    return shippingInstructionMapper.toStatusDTO(updatedShippingInstruction);
  }

  public void resolveStuffing(
    ShippingInstruction shippingInstruction,
    Map<String, UtilizedTransportEquipment> savedTransportEquipments) {

    for (var ci : shippingInstruction.getConsignmentItems()) {
      ci.resolvedShipment(resolveShipment(ci.getCarrierBookingReference()));
      for (var c : ci.getCargoItems()) {
        c.assignEquipment(findSavedUtilizedTransportEquipmentViaCargoItem(savedTransportEquipments, c));
      }
    }
  }

  // A Shipping instruction must link to a shipment (=approved booking) consignmentItems acts like a
  // 'join-table' between shipping instruction and shipment
  private Shipment resolveShipment(String carrierBookingReference) {
    return shipmentRepository
      .findByCarrierBookingReference(carrierBookingReference)
      .orElseThrow(
        () ->
          // TODO: This should result in a PENDING UPDATE rather than a HTTP 400 by the time DT-578 is finished
          ConcreteRequestErrorMessageException.invalidInput(
            "No shipment has been found for this carrierBookingReference: "
              + carrierBookingReference));
  }

  private UtilizedTransportEquipment findSavedUtilizedTransportEquipmentViaCargoItem(
    Map<String, UtilizedTransportEquipment> savedTransportEquipments, CargoItem cargoItem) {
    UtilizedTransportEquipment ute = savedTransportEquipments.get(cargoItem.getEquipmentReference());
    if (ute == null) {
      // TODO: This should result in a PENDING UPDATE rather than a HTTP 400 by the time DT-578 is finished
      throw ConcreteRequestErrorMessageException.invalidInput(
        "Could not find utilizedTransportEquipments for this cargoItems, based on equipmentReference: "
          + cargoItem.getEquipmentReference());
    }
    return ute;
  }
}
