package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.awt.print.Book;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface ReferenceMapper {

  @Mapping(source = "booking", target = "booking")
  @Mapping(source = "shippingInstruction.id", target = "shippingInstructionID")
  @Mapping(target = "referenceID", ignore = true)
  Reference toDAO(ReferenceTO referenceTO, Booking booking, ShippingInstruction shippingInstruction, ConsignmentItem consignmentItem);

  default Reference toDAO(ReferenceTO referenceTO, Booking booking) {
    return toDAO(referenceTO, booking, null, null);
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
