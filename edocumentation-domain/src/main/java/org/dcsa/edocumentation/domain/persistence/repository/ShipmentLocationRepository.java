package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentLocationRepository extends JpaRepository<ShipmentLocation, UUID> {
  Optional<ShipmentLocation> findByShipmentID(UUID shipmentID);
}
