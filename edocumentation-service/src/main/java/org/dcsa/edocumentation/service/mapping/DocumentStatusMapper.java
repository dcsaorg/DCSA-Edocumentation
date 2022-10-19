package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentStatusMapper {
  org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus toDomainBkgDocumentStatus(BkgDocumentStatus documentStatus);
}
