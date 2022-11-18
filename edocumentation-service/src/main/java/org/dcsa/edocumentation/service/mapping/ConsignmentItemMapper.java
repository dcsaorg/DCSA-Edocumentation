package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsignmentItemMapper {

  @Mapping(source = "cargoItems", target = "cargoItems", ignore = true)
  ConsignmentItem toDAO(ConsignmentItemTO consignmentItemTO);
}
