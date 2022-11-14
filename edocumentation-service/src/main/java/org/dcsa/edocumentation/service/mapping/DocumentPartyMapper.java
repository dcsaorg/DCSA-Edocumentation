package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.PartyContactDetails;
import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentPartyMapper {
  @Mapping(source = "documentPartyTO.displayedAddress", target = "displayedAddress", ignore = true)
  DocumentParty toDAO(DocumentPartyTO documentPartyTO);

  @Mapping(source = "address", target = "address", ignore = true)
  @Mapping(source = "partyContactDetails", target = "partyContactDetails", ignore = true)
  @Mapping(source = "identifyingCodes", target = "identifyingCodes", ignore = true)
  Party toDAO(PartyTO partyTO);

  @Mapping(source = "party", target = "party")
  @Mapping(source = "party.id", target = "id", ignore = true)
  PartyContactDetails toDAO(PartyContactDetailsTO partyContactDetailsTO, Party party);

  @Mapping(source = "party", target = "party")
  @Mapping(source = "party.id", target = "id", ignore = true)
  PartyIdentifyingCode toDAO(PartyIdentifyingCodeTO partyIdentifyingCodeTO, Party party);
}
