package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransportDocumentRepository
    extends JpaRepository<TransportDocument, UUID>, JpaSpecificationExecutor<TransportDocument> {

  @EntityGraph("graph.transportDocumentSummary")
  List<TransportDocument> findAllById(Iterable<UUID> ids);

  Optional<TransportDocument> findByTransportDocumentReferenceAndValidUntilIsNull(String reference);
}
