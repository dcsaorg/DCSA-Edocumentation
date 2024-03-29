package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShippingInstructionRepository  extends JpaRepository<ShippingInstruction, UUID>,
  JpaSpecificationExecutor<ShippingInstruction> {

  @EntityGraph("graph.shippingInstructionSummary")
  List<ShippingInstruction> findAllById(Iterable<UUID> ids);

  Optional<ShippingInstruction> findByShippingInstructionReferenceAndValidUntilIsNull(String shippingInstructionReference);
}
