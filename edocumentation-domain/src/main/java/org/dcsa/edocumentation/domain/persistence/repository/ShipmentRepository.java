package org.dcsa.edocumentation.domain.persistence.repository;

import lombok.NonNull;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

  @EntityGraph("graph.shipment-summary")
  Page<Shipment> findAllByBookingDocumentStatus(
      @NonNull BkgDocumentStatus documentStatus, Pageable pageable);

  @EntityGraph("graph.shipment-summary")
  Page<Shipment> findAll(Pageable pageable);
  Optional<Shipment> findByCarrierBookingReference(String carrierBookingReference);
  @EntityGraph("graph.shipment")
  Optional<Shipment> findShipmentByCarrierBookingReference(String carrierBookingReference);
}
