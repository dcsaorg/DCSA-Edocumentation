package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ValueAddedServiceRequestRepository extends JpaRepository<ValueAddedService, UUID> { }
