package org.dcsa.edocumentation.domain.persistence.repository;

import java.util.Optional;
import java.util.UUID;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

  @EntityGraph("graph.booking")
  Optional<Booking> findByCarrierBookingRequestReference(String carrierBookingRequestReference);

  @EntityGraph("graph.booking")
  Optional<Booking> findByCarrierBookingReference(String carrierBookingReference);
}
