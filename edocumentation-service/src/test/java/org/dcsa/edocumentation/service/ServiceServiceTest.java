package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ServiceDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.Service;
import org.dcsa.edocumentation.domain.persistence.repository.ServiceRepository;
import org.dcsa.edocumentation.transferobjects.BookingRequestTO;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

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
public class ServiceServiceTest {
  @Mock
  private ServiceRepository serviceRepository;
  @InjectMocks
  private ServiceService serviceService;

  @BeforeEach
  public void resetMocks() {
    reset(serviceRepository);
  }

  @Test
  public void resolveService_null() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .universalServiceReference(null)
      .carrierServiceCode(null)
      .build();

    // Execute
    assertNull(serviceService.resolveService(bookingRequestTO));

    // Verify
    verify(serviceRepository, never()).findAll(any(Example.class));
  }

  @Test
  public void resolveService_Unknown() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .universalServiceReference("serviceRef")
      .carrierServiceCode("carrierRef")
      .build();

    // Execute
    NotFoundException exception =
      assertThrows(NotFoundException.class, () -> serviceService.resolveService(bookingRequestTO));

    // Verify
    assertEquals("No services that match carrierServiceCode 'carrierRef' and universalServiceReference 'serviceRef'", exception.getMessage());
    verify(serviceRepository).findAll(any(Example.class));
  }

  @Test
  public void resolveService_Known() {
    // Setup
    BookingRequestTO bookingRequestTO = BookingRequestTO.builder()
      .universalServiceReference("serviceRef")
      .carrierServiceCode("carrierRef")
      .build();
    Service expected = ServiceDataFactory.service();
    when(serviceRepository.findAll(any(Example.class))).thenReturn(List.of(expected));

    // Execute
    Service actual = serviceService.resolveService(bookingRequestTO);

    // Verify
    assertEquals(expected, actual);
    verify(serviceRepository).findAll(any(Example.class));
  }
}
