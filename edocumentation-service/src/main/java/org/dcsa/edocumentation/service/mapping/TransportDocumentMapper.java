package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.transferobjects.unofficial.TransportDocumentRefStatusTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransportDocumentMapper {

  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  TransportDocumentRefStatusTO toStatusDTO(TransportDocument transportDocument);
}
