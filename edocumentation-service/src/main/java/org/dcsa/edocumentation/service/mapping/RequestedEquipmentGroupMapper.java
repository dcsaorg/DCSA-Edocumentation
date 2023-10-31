package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.RequestedEquipmentGroup;
import org.dcsa.edocumentation.transferobjects.RequestedEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    ActiveReeferSettingsMapper.class
  }
)
public abstract class RequestedEquipmentGroupMapper {

  @Autowired
  protected CommodityMapper commodityMapper;

  @Mapping(target = "bookingData", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "commodities", ignore = true)
  protected abstract RequestedEquipmentGroup toDAOInternal(RequestedEquipmentTO requestedEquipment);

  public RequestedEquipmentGroup toDAO(RequestedEquipmentTO requestedEquipmentTO) {
    var reg = this.toDAOInternal(requestedEquipmentTO) ;
    reg.assignCommodities(requestedEquipmentTO.commodities().stream().map(commodityMapper::toDAO).toList());
    return reg;
  }

  public abstract RequestedEquipmentTO toTO(RequestedEquipmentGroup requestedEquipment);

}
