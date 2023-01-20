package org.dcsa.edocumentation.domain.decoupled.repository;

import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("decoupledBookingRepository")
public interface BookingRepository extends JpaRepository<Booking, UUID> {
  @Query("FROM Booking WHERE carrierBookingRequestReference = :carrierBookingRequestReference AND validUntil IS NULL")
  Optional<Booking> findByCarrierBookingRequestReference(String carrierBookingRequestReference);
}
