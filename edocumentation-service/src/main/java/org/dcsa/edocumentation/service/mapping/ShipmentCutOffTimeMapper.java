package org.dcsa.edocumentation.service.mapping;


import org.dcsa.edocumentation.domain.persistence.entity.ShipmentCutOffTime;
import org.dcsa.edocumentation.transferobjects.ShipmentCutOffTimeTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ShipmentCutOffTimeMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "bookingData", ignore = true)
  ShipmentCutOffTime toDAO(ShipmentCutOffTimeTO shipmentCutOffTimeTO);

  ShipmentCutOffTimeTO toTO(ShipmentCutOffTime shipmentCutOffTime);
}
