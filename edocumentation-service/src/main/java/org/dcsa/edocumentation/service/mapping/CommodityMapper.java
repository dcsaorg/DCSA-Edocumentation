package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Map;

@Mapper(componentModel = "spring", uses = CommodityRequestedEquipmentLinkMapper.class)
public interface CommodityMapper {
  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "booking.id", target = "id", ignore = true)
  @Mapping(expression = "java(mapCommodityRequestedEquipmentLink(links, commodityTO.commodityRequestedEquipmentLink()))", target = "commodityRequestedEquipmentLink")
  Commodity toDAO(CommodityTO commodityTO, Booking booking, Map<String, CommodityRequestedEquipmentLink> links);

  default CommodityRequestedEquipmentLink mapCommodityRequestedEquipmentLink(
    Map<String, CommodityRequestedEquipmentLink> links, String commodityRequestedEquipmentLink
  ) {
    return commodityRequestedEquipmentLink == null ? null : links.get(commodityRequestedEquipmentLink);
  }
}
