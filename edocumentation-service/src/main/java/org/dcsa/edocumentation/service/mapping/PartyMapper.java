package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.PartyContactDetails;
import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    AddressMapper.class,
    PartyContactDetailsMapper.class,
    PartyIdentifyingCodeMapper.class,
  })
public interface PartyMapper {
  @Mapping(source = "partyContactDetails", target = "partyContactDetails", ignore = true)
  @Mapping(source = "identifyingCodes", target = "identifyingCodes", ignore = true)
  @Mapping(target = "id", ignore = true)
  Party toDAO(PartyTO partyTo);
  PartyTO toTO(Party party);


  @Mapping(source = "party", target = "party")
  @Mapping(source = "party.id", target = "id", ignore = true)
  PartyContactDetails toDAO(PartyContactDetailsTO partyContactDetailsTO, Party party);

  @Mapping(source = "party", target = "party")
  @Mapping(source = "party.id", target = "id", ignore = true)
  PartyIdentifyingCode toDAO(PartyIdentifyingCodeTO partyIdentifyingCodeTO, Party party);
}

