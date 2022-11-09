package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ShipmentLocationService {
  private final LocationService locationService;
  private final ShipmentLocationRepository shipmentLocationRepository;
  private final ShipmentLocationMapper shipmentLocationMapper;

  public void createShipmentLocations(Collection<ShipmentLocationTO> shipmentLocations, Booking booking) {
    if (shipmentLocations != null && !shipmentLocations.isEmpty()) {
      shipmentLocationRepository.saveAll(
        shipmentLocations.stream()
          .map(sl -> shipmentLocationMapper.toDAO(sl, booking).toBuilder()
            .location(locationService.ensureResolvable(sl.location()))
            .build())
          .toList()
      );
    }
  }
}
