package org.dcsa.edocumentation.domain.decoupled.repository;

import org.dcsa.edocumentation.domain.decoupled.entity.ShippingInstruction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository("decoupledShippingInstructionRepository")
public interface ShippingInstructionRepository extends JpaRepository<ShippingInstruction, UUID>, JpaSpecificationExecutor<ShippingInstruction> {
  @Query("FROM ShippingInstruction WHERE shippingInstructionReference = :shippingInstructionReference AND validUntil IS NULL")
  Optional<ShippingInstruction> findByShippingInstructionReference(String shippingInstructionReference);
}
