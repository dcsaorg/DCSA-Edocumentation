package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.PartyContactDetails;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class
)
public interface PartyContactDetailsMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "party", ignore = true)
  PartyContactDetails toDAO(PartyContactDetailsTO partyContactDetailsTO);
}
