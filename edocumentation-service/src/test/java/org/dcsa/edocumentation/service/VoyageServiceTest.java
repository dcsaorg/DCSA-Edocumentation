package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.VoyageDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoyageServiceTest {
  @Mock private VoyageRepository voyageRepository;

  @InjectMocks private VoyageService voyageService;

  @BeforeEach
  public void resetMocks() {
    reset(voyageRepository);
  }

  @Test
  public void resolveVoyage_null() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .carrierExportVoyageNumber(null)
      .build();

    // Execute
    assertNull(voyageService.resolveVoyage(bookingTO));

    // Verify
    verify(voyageRepository, never()).findByCarrierVoyageNumber(any());
  }

  @Test
  public void resolveVoyage_unknown() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .carrierExportVoyageNumber("voyageRef")
      .build();
    when(voyageRepository.findByCarrierVoyageNumber(any())).thenReturn(Collections.emptyList());

    // Execute
    NotFoundException exception =
      assertThrows(NotFoundException.class, () -> voyageService.resolveVoyage(bookingTO));

    // Verify
    assertEquals("No voyages with carrierVoyageNumber = 'voyageRef'", exception.getMessage());
    verify(voyageRepository).findByCarrierVoyageNumber("voyageRef");
  }

  @Test
  public void resolveVoyage_known() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .carrierExportVoyageNumber("voyageRef")
      .build();
    Voyage expected = VoyageDataFactory.voyage();
    when(voyageRepository.findByCarrierVoyageNumber(any())).thenReturn(List.of(expected));

    // Execute
    Voyage actual = voyageService.resolveVoyage(bookingTO);

    // Verify
    assertEquals(expected, actual);
    verify(voyageRepository).findByCarrierVoyageNumber("voyageRef");
  }
}