package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ActiveReeferSettings;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(
  componentModel = "spring",
  uses = {
    CommodityRequestedEquipmentLinkMapper.class,
    ActiveReeferSettingsMapper.class
  }
)
public interface RequestedEquipmentGroupMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(source = "requestedEquipment.isoEquipmentCode", target = "requestedEquipmentIsoEquipmentCode")
  @Mapping(source = "requestedEquipment.units", target = "requestedEquipmentUnits")
  @Mapping(expression = "java(mapCommodityRequestedEquipmentLink(links, requestedEquipment.commodityRequestedEquipmentLink()))", target = "commodityRequestedEquipmentLink")
  RequestedEquipmentGroup toDAO(
    RequestedEquipmentTO requestedEquipment,
    Booking booking,
    ActiveReeferSettings activeReeferSettings,
    Map<String, CommodityRequestedEquipmentLink> links
  );

  default CommodityRequestedEquipmentLink mapCommodityRequestedEquipmentLink(
    Map<String, CommodityRequestedEquipmentLink> links, String commodityRequestedEquipmentLink
  ) {
    return commodityRequestedEquipmentLink == null ? null : links.get(commodityRequestedEquipmentLink);
  }

  @Mapping(source = "requestedEquipmentUnits", target = "units")
  @Mapping(source = "requestedEquipmentIsoEquipmentCode", target = "isoEquipmentCode")
  @Mapping(source = "requestedEquipment.commodityRequestedEquipmentLink.commodityRequestedEquipmentLink", target = "commodityRequestedEquipmentLink")
  RequestedEquipmentTO toTO(RequestedEquipmentGroup requestedEquipment);

  default String mapEquipmentToEquipmentReference(Equipment equipment) {
    return equipment.getEquipmentReference();
  }
}
