package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.dcsa.edocumentation.transferobjects.PartyContactDetailsTO;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DocumentPartyMapper {

  //ToDO can be removed when booking takes the same approach as Shipping instruction for setting the FK
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(source = "documentPartyTO.displayedAddress", target = "displayedAddress", ignore = true)
  @Mapping(source = "documentPartyTO.party", target = "party", ignore = true)
  DocumentParty toDAO(DocumentPartyTO documentPartyTO, Booking booking);

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
