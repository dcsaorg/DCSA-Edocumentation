package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UtilizedTransportEquipmentRepository extends JpaRepository<UtilizedTransportEquipment, UUID> {
}
