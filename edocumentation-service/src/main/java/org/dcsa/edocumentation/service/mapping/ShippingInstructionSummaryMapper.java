package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingInstructionSummaryMapper {
  @Mapping(
    source = "consignmentItems",
    target = "carrierBookingReferences")
  ShippingInstructionSummaryTO shippingInstructionToShippingInstructionSummary(ShippingInstruction shippingInstruction);

  default String mapShipmentToCarrierBookingReference(ConsignmentItem consignmentItem) {
    return consignmentItem.getShipment().getCarrierBookingReference();
  }
}
