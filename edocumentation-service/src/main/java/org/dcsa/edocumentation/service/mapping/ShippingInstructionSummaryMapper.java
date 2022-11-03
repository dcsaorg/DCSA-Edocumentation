package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingInstructionSummaryMapper {
  @Mapping(
    source = "shipments",
    target = "carrierBookingReferences")
  ShippingInstructionSummaryTO shippingInstructionToShippingInstructionSummary(ShippingInstruction shippingInstruction);

  default String mapShipmentToCarrierBookingReference(Shipment shipment) {
    return shipment.getCarrierBookingReference();
  }
}