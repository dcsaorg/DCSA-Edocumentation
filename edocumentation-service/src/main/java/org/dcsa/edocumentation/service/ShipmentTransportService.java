package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentTransportRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentTransportMapper;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.skernel.infrastructure.services.LocationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentTransportService {
  private final ShipmentTransportRepository shipmentTransportRepository;
  private final ShipmentTransportMapper shipmentTransportMapper;
  private final LocationService locationService;

  @Transactional(TxType.MANDATORY)
  public void createShipmentTransports(Collection<TransportTO> shipmentTransports, Shipment shipment) {
    if (shipmentTransports != null && !shipmentTransports.isEmpty()) {
      shipmentTransportRepository.saveAll(
        shipmentTransports.stream()
          .map(
            sl ->
              shipmentTransportMapper.toDAO(
                sl,
                shipment,
                locationService.ensureResolvable(sl.loadLocation()),
                locationService.ensureResolvable(sl.dischargeLocation())
              ))
          .toList());
    }
  }

}
