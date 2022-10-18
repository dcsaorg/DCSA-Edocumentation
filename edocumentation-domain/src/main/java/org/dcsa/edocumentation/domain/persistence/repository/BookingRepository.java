package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.NonNull;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
  Page<Booking> findAllByDocumentStatus(
      @NonNull BkgDocumentStatus documentStatus, Pageable pageable);
}
