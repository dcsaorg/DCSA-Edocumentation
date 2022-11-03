package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.repository.specification.ShippingInstructionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface ShippingInstructionRepository  extends JpaRepository<ShippingInstruction, UUID> {
  @EntityGraph("graph.shipping-instruction-summary")
  Page<ShippingInstruction> findAll(@Nullable Specification<ShippingInstructionSpecification> spec, Pageable pageable);
}
