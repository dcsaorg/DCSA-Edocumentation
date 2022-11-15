package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VoyageRepository extends JpaRepository<Voyage, UUID> {
  List<Voyage> findByUniversalVoyageReference(String universalVoyageReference);
  List<Voyage> findByCarrierVoyageNumber(String carrierVoyageNumber);
}
