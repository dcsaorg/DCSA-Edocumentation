package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.transferobjects.ActiveReeferSettingsTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActiveReeferSettingsMapper {
  ActiveReeferSettings toDAO(ActiveReeferSettingsTO activeReeferSettings);
  ActiveReeferSettingsTO toDTO(ActiveReeferSettings activeReeferSettings);
}
