package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.VoyageDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Voyage;
import org.dcsa.edocumentation.domain.persistence.repository.VoyageRepository;
import org.dcsa.edocumentation.transferobjects.BookingTO;
import org.dcsa.skernel.errors.exceptions.BadRequestException;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

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
  @Mock private ServiceService serviceService;
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
      .universalExportVoyageReference(null)
      .carrierExportVoyageNumber(null)
      .build();

    // Execute
    assertNull(voyageService.resolveVoyage(bookingTO));

    // Verify
    verify(voyageRepository, never()).findAll(any(Example.class));
    verify(serviceService, never()).resolveService(any());
  }

  @Test
  public void resolveVoyage_nullWithServiceArgs() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .universalServiceReference("serviceRef")
      .carrierServiceCode("carrierRef")
      .build();

    // Execute
    BadRequestException exception =
      assertThrows(BadRequestException.class, () -> voyageService.resolveVoyage(bookingTO));

    // Verify
    assertEquals("carrierServiceCode and/or universalServiceReference provided but both universalExportVoyageReference and carrierExportVoyageNumber are missing", exception.getMessage());
    verify(voyageRepository, never()).findAll(any(Example.class));
    verify(serviceService, never()).resolveService(any());
  }

  @Test
  public void resolveVoyage_Unknown() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .universalExportVoyageReference("voyageRef")
      .carrierExportVoyageNumber("carrierRef")
      .build();
    when(voyageRepository.findAll(any(Example.class))).thenReturn(Collections.emptyList());

    // Execute
    NotFoundException exception =
      assertThrows(NotFoundException.class, () -> voyageService.resolveVoyage(bookingTO));

    // Verify
    assertEquals("No voyages with universalVoyageReference = 'voyageRef' and carrierExportVoyageNumber = 'carrierRef'", exception.getMessage());
    verify(voyageRepository).findAll(any(Example.class));
    verify(serviceService).resolveService(any());
  }

  @Test
  public void resolveVoyage_Known() {
    // Setup
    BookingTO bookingTO = BookingTO.builder()
      .universalExportVoyageReference(null)
      .carrierExportVoyageNumber("voyageRef")
      .build();
    Voyage expected = VoyageDataFactory.voyage();
    when(voyageRepository.findAll(any(Example.class))).thenReturn(List.of(expected));

    // Execute
    Voyage actual = voyageService.resolveVoyage(bookingTO);

    // Verify
    assertEquals(expected, actual);
    verify(voyageRepository).findAll(any(Example.class));
    verify(serviceService).resolveService(any());
  }
}
