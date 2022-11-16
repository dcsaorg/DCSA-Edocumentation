package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.transferobjects.PartyTO;
import org.dcsa.skernel.infrastructure.services.mapping.AddressMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface PartyMapper {
  Party toDao(PartyTO partyTo);
  PartyTO toTO(Party party);
}

