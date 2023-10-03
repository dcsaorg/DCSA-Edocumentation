package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface DocumentStatusMapper {
  org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus toDomainEblDocumentStatus(EblDocumentStatus documentStatus);

  EblDocumentStatus toTransferEblDocumentStatus(org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus documentStatus);
  ShipmentLocationTypeCode toTOShipmentLocationTypeCode(org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType
                                                          shipmentLocationTypeCode);
}
