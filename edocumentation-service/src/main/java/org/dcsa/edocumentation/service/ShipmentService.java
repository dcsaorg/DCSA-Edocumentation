package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ShipmentService {
  private final ShipmentRepository shipmentRepository;
  private final ShipmentMapper shipmentMapper;

  public ShipmentTO findShipment(String carrierBookingReference) {
    return shipmentRepository
        .findShipmentByCarrierBookingReference(carrierBookingReference)
        .map(shipmentMapper::shipmentToShipmentTO)
        .orElseThrow(
            () ->
                ConcreteRequestErrorMessageException.notFound(
                    "No Shipment found with carrierBookingReference = " + carrierBookingReference));
  }
}
