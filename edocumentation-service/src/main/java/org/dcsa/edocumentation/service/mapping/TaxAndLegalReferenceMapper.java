package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.PartyIdentifyingCode;
import org.dcsa.edocumentation.domain.persistence.entity.TaxAndLegalReference;
import org.dcsa.edocumentation.transferobjects.PartyIdentifyingCodeTO;
import org.dcsa.edocumentation.transferobjects.TaxAndLegalReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class
)
public interface TaxAndLegalReferenceMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "party", ignore = true)
  TaxAndLegalReference toDAO(TaxAndLegalReferenceTO taxAndLegalReferenceTO);

  TaxAndLegalReferenceTO toTO(TaxAndLegalReference taxAndLegalReference);
}
