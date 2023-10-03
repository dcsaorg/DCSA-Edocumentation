package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface CommodityMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "requestedEquipment", ignore = true)
  Commodity toDAO(CommodityTO commodityTO);

}
