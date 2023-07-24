package org.dcsa.edocumentation.domain.persistence.repository;

import java.util.UUID;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentLocationRepository extends JpaRepository<ShipmentLocation, UUID> {
}
