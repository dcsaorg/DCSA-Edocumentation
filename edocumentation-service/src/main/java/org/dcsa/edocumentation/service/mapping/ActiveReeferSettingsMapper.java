package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.transferobjects.ActiveReeferSettingsTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ActiveReeferSettingsMapper {
  @Mapping(target = "id", ignore = true)
  ActiveReeferSettings toDAO(ActiveReeferSettingsTO activeReeferSettings);
  ActiveReeferSettingsTO toDTO(ActiveReeferSettings activeReeferSettings);
}
