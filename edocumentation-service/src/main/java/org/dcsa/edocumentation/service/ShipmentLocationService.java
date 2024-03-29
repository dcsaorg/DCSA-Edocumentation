package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedBooking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentLocationRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentLocationMapper;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentLocationService {
  private final ShipmentLocationRepository shipmentLocationRepository;
  private final ShipmentLocationMapper shipmentLocationMapper;

  @Transactional(TxType.MANDATORY)
  public void createShipmentLocations(
      Collection<ShipmentLocationTO> shipmentLocations, BookingRequest bookingRequest) {
    createShipments(shipmentLocations,
            (slTO) -> this.shipmentLocationMapper.toDAO(slTO, bookingRequest)
    );
  }

  @Transactional(TxType.MANDATORY)
  public void createShipmentLocations(Collection<ShipmentLocationTO> shipmentLocations, ConfirmedBooking confirmedBooking) {
    createShipments(shipmentLocations,
      (slTO) -> this.shipmentLocationMapper.toDAO(slTO, confirmedBooking)
    );
  }

  private void createShipments(Collection<ShipmentLocationTO> shipmentLocations,
                               Function<ShipmentLocationTO, ShipmentLocation> mapper) {
    if (shipmentLocations != null && !shipmentLocations.isEmpty()) {
      shipmentLocationRepository.saveAll(
        shipmentLocations.stream()
          .map(mapper)
          .toList());
    }
  }
}
