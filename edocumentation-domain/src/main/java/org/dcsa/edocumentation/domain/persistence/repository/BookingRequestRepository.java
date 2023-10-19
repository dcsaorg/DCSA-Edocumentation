package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.NonNull;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest, UUID> {
  @EntityGraph("graph.booking-request-summary")
  @Query(
    value = "FROM BookingRequest WHERE bookingStatus = :bookingStatus AND validUntil IS NULL",
    countQuery = "SELECT count(*) FROM BookingRequest WHERE bookingStatus = :bookingStatus AND validUntil IS NULL"
  )
  Page<BookingRequest> findAllByBookingStatus(
      @NonNull String bookingStatus, Pageable pageable);

  @EntityGraph("graph.booking-request-summary")
  @Query(
    value = "FROM BookingRequest WHERE validUntil IS NULL",
    countQuery = "SELECT count(*) FROM BookingRequest WHERE validUntil IS NULL"
  )
  @Override
  Page<BookingRequest> findAll(Pageable pageable);

  @EntityGraph("graph.booking-request")
  @Query("FROM BookingRequest WHERE carrierBookingRequestReference = :carrierBookingRequestReference AND validUntil IS NULL")
  Optional<BookingRequest> findBookingByCarrierBookingRequestReference(String carrierBookingRequestReference);
}
