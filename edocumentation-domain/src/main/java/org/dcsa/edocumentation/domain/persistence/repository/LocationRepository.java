package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
  List<Location> findByLocationNameAndFacilityAndUNLocationCode(String locationName, Facility facility, String UNLocationCode);

  @Query("FROM Location WHERE UNLocationCode = :UNLocationCode AND locationName = :locationName AND facility IS NULL")
  List<Location> findByLocationNameAndUNLocationCode(String locationName, String UNLocationCode);

  List<Location> findByLocationNameAndAddress(String locationName, Address address);
}
