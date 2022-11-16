package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsignmentItemMapper {

  ConsignmentItem toDAO(ConsignmentItemTO consignmentItemTO);
}
