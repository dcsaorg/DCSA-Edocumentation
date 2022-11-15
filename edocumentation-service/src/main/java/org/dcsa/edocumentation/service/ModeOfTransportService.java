package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.dcsa.edocumentation.domain.persistence.repository.ModeOfTransportRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModeOfTransportService {
  private final ModeOfTransportRepository modeOfTransportRepository;

  public ModeOfTransport resolveModeOfTransport(DCSATransportType dcsaTransportType) {
    if (dcsaTransportType != null) {
      return modeOfTransportRepository.findByDcsaTransportType(dcsaTransportType)
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No ModeOfTransport found for TransportCode = '" + dcsaTransportType + "'"));
    }
    return null;
  }
}