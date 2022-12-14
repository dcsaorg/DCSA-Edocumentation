package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.TransportDocument;
import org.dcsa.edocumentation.transferobjects.TransportDocumentTO;
import org.dcsa.edocumentation.transferobjects.unofficial.TransportDocumentRefStatusTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, ShippingInstructionMapper.class})
public interface TransportDocumentMapper {

  @Mapping(source = "transportDocument.shippingInstruction.documentStatus", target = "documentStatus")
  TransportDocumentRefStatusTO toStatusDTO(TransportDocument transportDocument);

  TransportDocumentTO toDTO(TransportDocument transportDocument);
}
