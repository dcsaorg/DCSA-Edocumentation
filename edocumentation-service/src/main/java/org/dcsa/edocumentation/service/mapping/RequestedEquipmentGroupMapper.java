package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  unmappedTargetPolicy = ReportingPolicy.WARN,  // FIXME: Remove when we are ready to make this an ERROR
  uses = {
    ActiveReeferSettingsMapper.class
  }
)
public interface RequestedEquipmentGroupMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(source = "activeReeferSettings", target = "activeReeferSettings")
  @Mapping(source = "requestedEquipment.isoEquipmentCode", target = "requestedEquipmentIsoEquipmentCode")
  @Mapping(source = "requestedEquipment.units", target = "requestedEquipmentUnits")
  @Mapping(source = "commodity", target = "commodity")
  // These are not provided at this stage
  @Mapping(target = "confirmedEquipmentUnits", ignore = true)
  @Mapping(target = "confirmedEquipmentIsoEquipmentCode", ignore = true)
  @Mapping(target = "shipment", ignore = true)
  @Mapping(target = "utilizedTransportEquipments", ignore = true)
  RequestedEquipmentGroup toDAO(
    RequestedEquipmentTO requestedEquipment,
    Booking booking,
    ActiveReeferSettings activeReeferSettings,
    Commodity commodity
  );

  @Mapping(source = "requestedEquipmentUnits", target = "units")
  @Mapping(source = "requestedEquipmentIsoEquipmentCode", target = "isoEquipmentCode")
  RequestedEquipmentTO toTO(RequestedEquipmentGroup requestedEquipment);

  default String mapEquipmentToEquipmentReference(Equipment equipment) {
    return equipment.getEquipmentReference();
  }
}
