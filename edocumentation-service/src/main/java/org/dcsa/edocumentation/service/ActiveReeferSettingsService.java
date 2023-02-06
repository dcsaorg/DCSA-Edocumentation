package org.dcsa.edocumentation.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.repository.ActiveReeferSettingsRepository;
import org.dcsa.edocumentation.service.mapping.ActiveReeferSettingsMapper;
import org.dcsa.edocumentation.transferobjects.ActiveReeferSettingsTO;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActiveReeferSettingsService {
  private final ActiveReeferSettingsRepository activeReeferSettingsRepository;
  private final ActiveReeferSettingsMapper mapper;

  @Transactional(Transactional.TxType.MANDATORY)
  public ActiveReeferSettings createActiveReeferSettings(ActiveReeferSettingsTO activeReeferSettingsTOS) {
    return activeReeferSettingsTOS == null ? null : activeReeferSettingsRepository.save(mapper.toDAO(activeReeferSettingsTOS));
  }
}
