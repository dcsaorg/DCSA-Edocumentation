package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.transferobjects.CommodityTO;
import org.dcsa.edocumentation.transferobjects.RequestedChangeTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface RequestedChangeMapper {

  // Never used in the official APIs as the attribute is read-only there.  But this
  // avoids the need to "ignore" them in the other mappers, plus makes them available
  // for unofficial endpoints.
  @Mapping(target = "id", ignore = true)
  BookingRequestedChange toBkgDAO(RequestedChangeTO change);

  // Never used in the official APIs as the attribute is read-only there.  But this
  // avoids the need to "ignore" them in the other mappers, plus makes them available
  //  // for unofficial endpoints.
  @Mapping(target = "id", ignore = true)
  ShippingInstructionRequestedChange toSIDAO(RequestedChangeTO change);

  RequestedChangeTO toTO(BookingRequestedChange change);
  RequestedChangeTO toTO(ShippingInstructionRequestedChange change);

}
