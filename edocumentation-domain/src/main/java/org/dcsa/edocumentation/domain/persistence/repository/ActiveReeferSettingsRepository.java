package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActiveReeferSettingsRepository extends JpaRepository<ActiveReeferSettings, UUID> { }
