package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.BookingData;
import org.dcsa.edocumentation.domain.persistence.repository.ShipmentTransportRepository;
import org.dcsa.edocumentation.service.mapping.ShipmentTransportMapper;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentTransportService {
  private final ShipmentTransportRepository shipmentTransportRepository;
  private final ShipmentTransportMapper shipmentTransportMapper;

  @Transactional(TxType.MANDATORY)
  public void createShipmentTransports(Collection<TransportTO> shipmentTransports, BookingData bookingData) {
    if (shipmentTransports != null && !shipmentTransports.isEmpty()) {
      shipmentTransportRepository.saveAll(
        shipmentTransports.stream()
          .map(
            sl ->
              shipmentTransportMapper.toDAO(
                sl,
                      bookingData,
                sl.loadLocation(),
                sl.dischargeLocation()
              ))
          .toList());
    }
  }

}
