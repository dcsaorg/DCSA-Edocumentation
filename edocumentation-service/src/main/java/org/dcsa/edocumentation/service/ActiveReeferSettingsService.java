package org.dcsa.edocumentation.service;

import lombok.AllArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.repository.ActiveReeferSettingsRepository;
import org.dcsa.edocumentation.service.mapping.ActiveReeferSettingsMapper;
import org.dcsa.edocumentation.transferobjects.BookingActiveReeferSettingsTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionActiveReeferSettingsTO;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ActiveReeferSettingsService {
  private final ActiveReeferSettingsRepository activeReeferSettingsRepository;
  private final ActiveReeferSettingsMapper mapper;

  @Transactional(Transactional.TxType.MANDATORY)
  public ActiveReeferSettings createBookingActiveReeferSettings(
      BookingActiveReeferSettingsTO activeReeferSettingsTOS) {
    return saveActiveReeferSettings(mapper.toDAO(activeReeferSettingsTOS));
  }

  @Transactional(Transactional.TxType.MANDATORY)
  public ActiveReeferSettings createShippingInstructionActiveReeferSettings(
      ShippingInstructionActiveReeferSettingsTO activeReeferSettingsTOS) {
    return saveActiveReeferSettings(mapper.toDAO(activeReeferSettingsTOS));
  }

  private ActiveReeferSettings saveActiveReeferSettings(ActiveReeferSettings activeReeferSettings) {
    return activeReeferSettingsRepository.save(activeReeferSettings);
  }
}
