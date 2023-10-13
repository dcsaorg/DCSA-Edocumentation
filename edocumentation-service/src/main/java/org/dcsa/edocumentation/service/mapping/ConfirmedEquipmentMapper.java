package org.dcsa.edocumentation.service.mapping;


import org.dcsa.edocumentation.domain.persistence.entity.ConfirmedEquipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentCutOffTime;
import org.dcsa.edocumentation.transferobjects.ConfirmedEquipmentTO;
import org.dcsa.edocumentation.transferobjects.ShipmentCutOffTimeTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ConfirmedEquipmentMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "confirmedBooking", ignore = true)
  ConfirmedEquipment toDAO(ConfirmedEquipmentTO shipmentCutOffTimeTO);

  ConfirmedEquipmentTO toTO(ConfirmedEquipment shipmentCutOffTime);
}
