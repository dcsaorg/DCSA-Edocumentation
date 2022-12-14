package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentLocationService {
  private final LocationService locationService;
  private final ShipmentLocationRepository shipmentLocationRepository;
  private final ShipmentLocationMapper shipmentLocationMapper;

  @Transactional(TxType.MANDATORY)
  public void createShipmentLocations(
      Collection<ShipmentLocationTO> shipmentLocations, Booking booking) {
    if (shipmentLocations != null && !shipmentLocations.isEmpty()) {
      shipmentLocationRepository.saveAll(
          shipmentLocations.stream()
              .map(
                  sl ->
                      shipmentLocationMapper.toDAO(sl, booking).toBuilder()
                          .location(locationService.ensureResolvable(sl.location()))
                          .build())
              .toList());
    }
  }

  @Transactional(TxType.MANDATORY)
  public List<ShipmentLocationTO> getShipmentLocations(List<Shipment> shipments) {
    return shipments.stream()
        .map(Shipment::getId)
        .map(shipmentLocationRepository::findByShipmentID)
        .map(
            shipmentLocation ->
                shipmentLocation.orElseThrow(
                    () ->
                        ConcreteRequestErrorMessageException.notFound(
                            "No shipment location found for shipment.")))
        .map(shipmentLocationMapper::toDTO)
        .toList();
  }
}
