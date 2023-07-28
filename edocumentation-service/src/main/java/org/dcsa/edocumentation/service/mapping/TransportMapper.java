package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    uses = {LocationMapper.class, DocumentStatusMapper.class, ModeOfTransportMapper.class})
public interface TransportMapper {

  @Mapping(
    source = "transportPlanStageCode",
    target = "transportPlanStage")
  @Mapping(target = "isUnderShippersResponsibility", ignore = true)  // FIXME: Verify if this should be mapped
  TransportTO shipmentTransportToTransportTO(ShipmentTransport shipmentTransport);
}
