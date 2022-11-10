package org.dcsa.edocumentation.domain.persistence;

import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, UUID> {
  Optional<Facility> findByUNLocationCodeAndFacilitySMDGCode(String UNLocationCode, String facilitySMDGCode);
  Optional<Facility> findByUNLocationCodeAndFacilityBICCode(String UNLocationCode, String facilityBICCode);
}
