package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.ActiveReeferSettingsDataFactory;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.repository.ActiveReeferSettingsRepository;
import org.dcsa.edocumentation.service.mapping.ActiveReeferSettingsMapper;
import org.dcsa.edocumentation.transferobjects.ActiveReeferSettingsTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ActiveReeferSettingsServiceTest {

  @Mock private ActiveReeferSettingsRepository repository;
  @Spy private ActiveReeferSettingsMapper mapper = Mappers.getMapper(ActiveReeferSettingsMapper.class);
  @InjectMocks private ActiveReeferSettingsService service;

  @Captor private ArgumentCaptor<ActiveReeferSettings> activeReeferSettingsCapture;

  @Test
  void activeReeferSettingsServiceTest_bkgFreezer() {
    ActiveReeferSettingsTO activeReeferSettingsTO = ActiveReeferSettingsDataFactory.activeReeferSettingsTO();

    service.createActiveReeferSettings(activeReeferSettingsTO);
    verify(repository).save(activeReeferSettingsCapture.capture());
  }
}
