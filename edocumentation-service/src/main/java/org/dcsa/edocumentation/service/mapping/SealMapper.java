package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Seal;
import org.dcsa.edocumentation.transferobjects.SealTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface SealMapper {

  @Mapping(target = "id", ignore = true)
  Seal toDAO(SealTO sealTO);

  SealTO toDTO(Seal seal);
}
