package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.NonNull;
import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedBooking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConfirmedBookingRepository extends JpaRepository<ConfirmedBooking, UUID> {

  @EntityGraph("graph.confirmed-booking-summary")
  Page<ConfirmedBooking> findAllByBookingDocumentStatus(
      @NonNull BkgDocumentStatus documentStatus, Pageable pageable);

  @EntityGraph("graph.confirmed-booking-summary")
  Page<ConfirmedBooking> findAll(Pageable pageable);
  Optional<ConfirmedBooking> findByCarrierBookingReference(String carrierBookingReference);
  @EntityGraph("graph.confirmed-booking")
  Optional<ConfirmedBooking> findConfirmedBookingByCarrierBookingReference(String carrierBookingReference);
}
