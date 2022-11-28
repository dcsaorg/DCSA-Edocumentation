package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.BookingActiveReeferSettingsDataFactory;
import org.dcsa.edocumentation.datafactories.ShippingInstructionActiveReeferSettingsDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.repository.ActiveReeferSettingsRepository;
import org.dcsa.edocumentation.service.mapping.ActiveReeferSettingsMapper;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActiveReeferSettingsServiceTest {

  @Mock private ActiveReeferSettingsRepository repository;
  @Spy private ActiveReeferSettingsMapper mapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);
  @InjectMocks private ActiveReeferSettingsService service;

  @Captor private ArgumentCaptor<ActiveReeferSettings> activeReeferSettingsCapture;

  @Test
  void activeReeferSettingsServiceTest_bkgFreezer() {
    BookingActiveReeferSettingsTO freezerTO = BookingActiveReeferSettingsDataFactory.bkgFreezer();

    service.createBookingActiveReeferSettings(freezerTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.FREZ.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_bkgSuperFreezer() {
    BookingActiveReeferSettingsTO superFreezerTO = BookingActiveReeferSettingsDataFactory.bkgSuperFreezer();

    service.createBookingActiveReeferSettings(superFreezerTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.SUPR.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_bkgRefrigerated() {
    BookingActiveReeferSettingsTO refrigeratedTO = BookingActiveReeferSettingsDataFactory.bkgRefrigerated();

    service.createBookingActiveReeferSettings(refrigeratedTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.REFR.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_bkgControlledAtmosphere() {
    BookingActiveReeferSettingsTO controlledAtmosphereTO = BookingActiveReeferSettingsDataFactory.bkgControlledAtmosphere();

    service.createBookingActiveReeferSettings(controlledAtmosphereTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.CONA.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_eblFreezer() {
    ShippingInstructionActiveReeferSettingsTO freezerTO = ShippingInstructionActiveReeferSettingsDataFactory.eblFreezer();

    service.createShippingInstructionActiveReeferSettings(freezerTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.FREZ.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_eblSuperFreezer() {
    ShippingInstructionActiveReeferSettingsTO superFreezerTO = ShippingInstructionActiveReeferSettingsDataFactory.eblSuperFreezer();

    service.createShippingInstructionActiveReeferSettings(superFreezerTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.SUPR.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_eblRefrigerated() {
    ShippingInstructionActiveReeferSettingsTO refrigeratedTO = ShippingInstructionActiveReeferSettingsDataFactory.eblRefrigerated();

    service.createShippingInstructionActiveReeferSettings(refrigeratedTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.REFR.name(), savedActiveReeferSettings.getType());
  }

  @Test
  void activeReeferSettingsServiceTest_eblControlledAtmosphere() {
    ShippingInstructionActiveReeferSettingsTO controlledAtmosphereTO = ShippingInstructionActiveReeferSettingsDataFactory.eblControlledAtmosphere();

    service.createShippingInstructionActiveReeferSettings(controlledAtmosphereTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
    ActiveReeferSettings savedActiveReeferSettings = activeReeferSettingsCapture.getValue();
    assertEquals(ReeferType.CONA.name(), savedActiveReeferSettings.getType());
  }
}
