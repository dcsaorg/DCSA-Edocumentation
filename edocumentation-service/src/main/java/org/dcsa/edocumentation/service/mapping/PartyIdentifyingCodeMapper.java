package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class
)
public interface PartyIdentifyingCodeMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "party", ignore = true)
  PartyIdentifyingCode toDAO(PartyIdentifyingCodeTO partyContactDetailsTO);
}
