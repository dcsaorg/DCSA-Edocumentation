package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.NonNull;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
  @EntityGraph("graph.booking-summary")
  @Query(
    value = "FROM Booking WHERE bookingStatus = :bookingStatus AND validUntil IS NULL",
    countQuery = "SELECT count(*) FROM Booking WHERE bookingStatus = :bookingStatus AND validUntil IS NULL"
  )
  Page<Booking> findAllByBookingStatus(
      @NonNull String bookingStatus, Pageable pageable);

  @EntityGraph("graph.booking-summary")
  @Query(
    value = "FROM Booking WHERE validUntil IS NULL",
    countQuery = "SELECT count(*) FROM Booking WHERE validUntil IS NULL"
  )
  @Override
  Page<Booking> findAll(Pageable pageable);

  @EntityGraph("graph.booking")
  @Query("FROM Booking WHERE carrierBookingRequestReference = :carrierBookingRequestReference AND validUntil IS NULL")
  Optional<Booking> findBookingByCarrierBookingRequestReference(String carrierBookingRequestReference);
}
