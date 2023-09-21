package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.AdvanceManifestFiling;
import org.dcsa.edocumentation.domain.persistence.entity.Location;
import org.dcsa.edocumentation.transferobjects.AdvanceManifestFilingTO;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface AdvanceManifestFilingMapper {
  AdvanceManifestFilingTO toDTO(AdvanceManifestFiling filing);
  @Mapping(target = "manifest_id", ignore = true)
  @Mapping(target = "shipment", ignore = true)
  AdvanceManifestFiling toDAO(AdvanceManifestFilingTO manifestFilingTO);
}
