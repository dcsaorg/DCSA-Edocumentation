package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  uses = {
    LocationMapper.class,
  })
public interface ShippingInstructionMapper {
  @Mapping(
    source = "shipments",
    target = "carrierBookingReferences")
  ShippingInstructionSummaryTO ShippingInstructionToShippingInstructionSummary(ShippingInstruction shippingInstruction);

  // TODO: Complete this stub mapping (DDT-1296)
  ShippingInstruction toDAO(ShippingInstructionTO shippingInstructionTO);

  // TODO: Complete this stub mapping (DDT-1296)
  ShippingInstructionTO toDTO(ShippingInstruction shippingInstruction);

  default String mapShipmentToCarrierBookingReference(Shipment shipment) {
    return shipment.getCarrierBookingReference();
  }

  ShippingInstructionRefStatusTO toStatusDTO(ShippingInstruction shippingInstruction);
}
