package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.domain.persistence.entity.ModeOfTransport;
import org.dcsa.edocumentation.domain.persistence.entity.enums.DCSATransportType;
import org.dcsa.edocumentation.domain.persistence.repository.ModeOfTransportRepository;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ModeOfTransportServiceTest {
  @Mock private ModeOfTransportRepository modeOfTransportRepository;

  @InjectMocks private ModeOfTransportService modeOfTransportService;

  @BeforeEach
  public void resetMocks() {
    reset(modeOfTransportRepository);
  }

  @Test
  void resolveModeOfTransport_null() {
    // Execute
    assertNull(modeOfTransportService.resolveModeOfTransport((DCSATransportType) null));

    // Verify
    verify(modeOfTransportRepository, never()).findByDcsaTransportType(any());
  }

  @Test
  void resolveModeOfTransport_unknown() {
    // Setup
    when(modeOfTransportRepository.findByDcsaTransportType(any())).thenReturn(Optional.empty());

    // Execute
    NotFoundException exception =
      assertThrows(NotFoundException.class, () -> modeOfTransportService.resolveModeOfTransport(DCSATransportType.VESSEL));

    // Verify
    assertEquals("No ModeOfTransport found for TransportCode = 'VESSEL'", exception.getMessage());
    verify(modeOfTransportRepository).findByDcsaTransportType(DCSATransportType.VESSEL);
  }

  @Test
  void resolveModeOfTransport_known() {
    // Setup
    ModeOfTransport expected = ModeOfTransport.builder()
      .dcsaTransportType(DCSATransportType.VESSEL)
      .build();
    when(modeOfTransportRepository.findByDcsaTransportType(any())).thenReturn(Optional.of(expected));

    // Execute
    ModeOfTransport actual = modeOfTransportService.resolveModeOfTransport(DCSATransportType.VESSEL);

    // Verify
    assertEquals(expected, actual);
    verify(modeOfTransportRepository).findByDcsaTransportType(DCSATransportType.VESSEL);
  }

  @Test
  void resolveModeOfTransport_knowTransportCode() {
    ModeOfTransport expected = ModeOfTransport.builder()
      .dcsaTransportType(DCSATransportType.VESSEL)
      .code("test_code")
      .build();
    when(modeOfTransportRepository.findByCode(any())).thenReturn(Optional.of(expected));

    // Execute
    ModeOfTransport actual = modeOfTransportService.resolveModeOfTransport("test_code");

    // Verify
    assertEquals(expected, actual);
    verify(modeOfTransportRepository).findByCode("test_code");
  }
}
