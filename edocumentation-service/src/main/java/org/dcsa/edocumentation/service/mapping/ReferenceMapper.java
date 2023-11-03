package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.*;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ReferenceMapper {

  @Mapping(source = "bookingData", target = "bookingData")
  @Mapping(source = "shippingInstruction.id", target = "shippingInstructionID")
  @Mapping(target = "referenceID", ignore = true)
  Reference toDAO(ReferenceTO referenceTO, BookingData bookingData, ShippingInstruction shippingInstruction, ConsignmentItem consignmentItem);

  default Reference toDAO(ReferenceTO referenceTO, BookingData bookingData) {
    return toDAO(referenceTO, bookingData, null, null);
  }

  default Reference toDAO(ReferenceTO referenceTO, ShippingInstruction shippingInstruction) {
    return toDAO(referenceTO, null, shippingInstruction, null);
  }

  default Reference toDAO(ReferenceTO referenceTO, ConsignmentItem consignmentItem) {
    return toDAO(referenceTO, null, null, consignmentItem);
  }

  default Reference toDAO(ReferenceTO referenceTO) {
    return toDAO(referenceTO, null, null, null);
  }
}
