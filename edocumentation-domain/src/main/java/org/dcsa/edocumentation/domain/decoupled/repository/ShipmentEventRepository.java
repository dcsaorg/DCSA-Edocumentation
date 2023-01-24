package org.dcsa.edocumentation.domain.decoupled.repository;

import org.dcsa.edocumentation.domain.decoupled.entity.ShipmentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("decoupledShipmentEventRepository")
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, UUID>, JpaSpecificationExecutor<ShipmentEvent> { }
