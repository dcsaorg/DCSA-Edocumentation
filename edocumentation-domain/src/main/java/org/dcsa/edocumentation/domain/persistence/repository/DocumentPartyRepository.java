package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentPartyRepository extends JpaRepository<DocumentParty, UUID> { }
