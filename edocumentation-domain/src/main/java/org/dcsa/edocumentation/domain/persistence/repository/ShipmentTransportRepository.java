package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentTransportRepository extends JpaRepository<ShipmentTransport, UUID> {

  List<ShipmentTransport> findByShipmentID(UUID shipmentID);
}
