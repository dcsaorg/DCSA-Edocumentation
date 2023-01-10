package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.dcsa.edocumentation.domain.persistence.repository.ModeOfTransportRepository;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ModeOfTransportService {
  private final ModeOfTransportRepository modeOfTransportRepository;

  @Transactional
  public ModeOfTransport resolveModeOfTransport(DCSATransportType dcsaTransportType) {
    if (dcsaTransportType != null) {
      return modeOfTransportRepository.findByDcsaTransportType(dcsaTransportType)
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No ModeOfTransport found for TransportCode = '" + dcsaTransportType + "'"));
    }
    return null;
  }

  @Transactional
  public ModeOfTransport resolveModeOfTransport(String modeOfTransportCode) {
    if (modeOfTransportCode != null) {
      return modeOfTransportRepository.findByCode(modeOfTransportCode)
        .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
          "No ModeOfTransport found for mode of transport code = '" + modeOfTransportCode + "'"));
    }
    return null;
  }
}
