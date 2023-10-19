package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.VesselDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Vessel;
import org.dcsa.edocumentation.domain.persistence.repository.VesselRepository;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
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
public class VesselServiceTest {
  @Mock private VesselRepository vesselRepository;

  @InjectMocks private VesselService vesselService;

  @BeforeEach
  public void resetMocks() {
    reset(vesselRepository);
  }

  @Test
  public void resolveVessel_null() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .vesselIMONumber(null)
      .build();

    // Execute
    assertNull(vesselService.resolveVessel(bookingRequestTO));

    // Verify
    verify(vesselRepository, never()).findByVesselIMONumber(any());
  }

  @Test
  public void resolveVessel_unknown() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .vesselIMONumber("1234567")
      .build();
    when(vesselRepository.findByVesselIMONumber(any())).thenReturn(Optional.empty());

    // Execute
    NotFoundException exception =
      assertThrows(NotFoundException.class, () -> vesselService.resolveVessel(bookingRequestTO));

    // Verify
    assertEquals("No vessel with vesselIMONumber = '1234567'", exception.getMessage());
    verify(vesselRepository).findByVesselIMONumber("1234567");
  }

  @Test
  public void resolveVessel_known() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .vesselIMONumber("1234567")
      .build();
    Vessel expected = VesselDataFactory.vessel();
    when(vesselRepository.findByVesselIMONumber(any())).thenReturn(Optional.of(expected));

    // Execute
    Vessel actual = vesselService.resolveVessel(bookingRequestTO);

    // Verify
    assertEquals(expected, actual);
    verify(vesselRepository).findByVesselIMONumber("1234567");
  }
}
