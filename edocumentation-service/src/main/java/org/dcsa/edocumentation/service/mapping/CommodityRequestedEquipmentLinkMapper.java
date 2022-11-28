package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CommodityRequestedEquipmentLink;
import org.springframework.stereotype.Component;

@Component
public class CommodityRequestedEquipmentLinkMapper {
  public String toDTO(CommodityRequestedEquipmentLink link) {
    return link == null ? null : link.getCommodityRequestedEquipmentLink();
  }
}
