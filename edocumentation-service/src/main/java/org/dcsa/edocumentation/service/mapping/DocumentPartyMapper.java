package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.transferobjects.DocumentPartyTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    DisplayedAddressMapper.class,
    PartyMapper.class,
})
public interface DocumentPartyMapper {

  @Mapping(source = "bookingRequest", target = "bookingRequest")
  @Mapping(source = "bookingRequest.id", target = "id", ignore = true)
  @Mapping(source = "documentPartyTO.displayedAddress", target = "displayedAddress", ignore = true)
  @Mapping(target = "shippingInstructionID", ignore = true)
  DocumentParty toDAO(DocumentPartyTO documentPartyTO, BookingRequest bookingRequest);

  default DocumentParty toDAO(DocumentPartyTO documentPartyTO) {
    return this.toDAO(documentPartyTO, null);
  }

  @Mapping(
    target = "displayedAddress"
  )
  DocumentPartyTO toTO(DocumentParty documentParty);
}
