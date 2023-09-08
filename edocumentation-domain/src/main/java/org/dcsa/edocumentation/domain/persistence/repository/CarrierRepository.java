package org.dcsa.edocumentation.domain.persistence.repository;

import java.util.UUID;

import org.dcsa.edocumentation.domain.persistence.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, UUID> {
  Carrier findBySmdgCode(String smdgCode);

  Carrier findByNmftaCode(String nmftaCode);
}
