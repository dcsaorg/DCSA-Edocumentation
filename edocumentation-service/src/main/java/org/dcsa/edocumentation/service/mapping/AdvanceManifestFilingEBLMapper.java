package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.AdvanceManifestFilingEBL;
import org.dcsa.edocumentation.transferobjects.AdvanceManifestFilingEBLTO;
import org.dcsa.edocumentation.transferobjects.AdvanceManifestFilingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface AdvanceManifestFilingEBLMapper {
  AdvanceManifestFilingTO toDTO(AdvanceManifestFilingEBL filing);
  @Mapping(target = "manifestId", ignore = true)
  @Mapping(target = "shippingInstruction", ignore = true)
  AdvanceManifestFilingEBL toDAO(AdvanceManifestFilingEBLTO manifestFilingTO);
}
