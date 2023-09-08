package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Location;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.service.mapping.LocationMapper;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentLocationService {
  private final LocationMapper locationMapper;
  private final ShipmentLocationRepository shipmentLocationRepository;
  private final ShipmentLocationMapper shipmentLocationMapper;

  @Transactional(TxType.MANDATORY)
  public void createShipmentLocations(
      Collection<ShipmentLocationTO> shipmentLocations, Booking booking) {
    createShipments(shipmentLocations,
      (slTO, loc) -> this.shipmentLocationMapper.toDAO(slTO, loc, booking)
    );
  }

  @Transactional(TxType.MANDATORY)
  public void createShipmentLocations(Collection<ShipmentLocationTO> shipmentLocations, Shipment shipment) {
    createShipments(shipmentLocations,
      (slTO, loc) -> this.shipmentLocationMapper.toDAO(slTO, loc, shipment)
    );
  }

  private void createShipments(Collection<ShipmentLocationTO> shipmentLocations,
                               BiFunction<ShipmentLocationTO, Location, ShipmentLocation> mapper) {
    if (shipmentLocations != null && !shipmentLocations.isEmpty()) {
      shipmentLocationRepository.saveAll(
        shipmentLocations.stream()
          .map(
            sl ->
              mapper.apply(sl, locationMapper.toDAO(sl.location())))
          .toList());
    }
  }
}
