package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.transferobjects.enums.BkgDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentStatusMapper {
  org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus toDomainBkgDocumentStatus(BkgDocumentStatus documentStatus);
  org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus toDomainEblDocumentStatus(EblDocumentStatus documentStatus);

  BkgDocumentStatus toTransferBkgDocumentStatus(org.dcsa.edocumentation.domain.persistence.entity.enums.BkgDocumentStatus documentStatus);
  EblDocumentStatus toTransferEblDocumentStatus(org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus documentStatus);
}
