package org.dcsa.edocumentation.domain.decoupled.repository;

import org.dcsa.edocumentation.domain.decoupled.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("decoupledBookingRepository")
public interface BookingRepository extends JpaRepository<Booking, UUID> {
  @Query("FROM Booking WHERE carrierBookingRequestReference = :carrierBookingRequestReference AND validUntil IS NULL")
  Optional<Booking> findByCarrierBookingRequestReference(String carrierBookingRequestReference);

  @Query(
    value = "FROM Booking WHERE validUntil IS NULL",
    countQuery = "SELECT count(*) FROM Booking WHERE validUntil IS NULL"
  )
  Page<Booking> findAll(Pageable pageable);

  @Query(
    value = "FROM Booking WHERE documentStatus = :documentStatus AND validUntil IS NULL",
    countQuery = "SELECT count(*) FROM Booking WHERE documentStatus = :documentStatus AND validUntil IS NULL"
  )
  Page<Booking> findAllByDocumentStatus(BkgDocumentStatus documentStatus, Pageable pageable);
}
