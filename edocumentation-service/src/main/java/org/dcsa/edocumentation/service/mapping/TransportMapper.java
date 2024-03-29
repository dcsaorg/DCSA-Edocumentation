package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ShipmentTransport;
import org.dcsa.edocumentation.transferobjects.TransportTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    uses = {LocationMapper.class})
public interface TransportMapper {

  @Mapping(
    source = "transportPlanStageCode",
    target = "transportPlanStage")
  TransportTO shipmentTransportToTransportTO(ShipmentTransport shipmentTransport);
}
