package org.dcsa.edocumentation.domain.persistence.repository;

import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeOfTransportRepository extends JpaRepository<ModeOfTransport, String> {
  Optional<ModeOfTransport> findByDcsaTransportType(DCSATransportType dcsaTransportType);
  Optional<ModeOfTransport> findByCode(String code);
}
