package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
  DisplayedAddressMapper.class
})
public interface DocumentPartyMapper {

  //ToDO can be removed when booking takes the same approach as Shipping instruction for setting the FK
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(source = "documentPartyTO.displayedAddress", target = "displayedAddress", ignore = true)
  @Mapping(source = "documentPartyTO.party", target = "party", ignore = true)
  DocumentParty toDAO(DocumentPartyTO documentPartyTO, Booking booking);

  @Mapping(source = "documentPartyTO.displayedAddress", target = "displayedAddress", ignore = true)
  DocumentParty toDAO(DocumentPartyTO documentPartyTO);

  @Mapping(
    target = "displayedAddress"
  )
  DocumentPartyTO toTO(DocumentParty documentParty);
}
