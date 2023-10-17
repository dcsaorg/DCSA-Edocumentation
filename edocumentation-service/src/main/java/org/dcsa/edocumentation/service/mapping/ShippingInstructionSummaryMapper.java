package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionSummaryTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    config = EDocumentationMappingConfig.class,
    uses = {DisplayedAddressMapper.class})
public interface ShippingInstructionSummaryMapper {
  @Mapping(
    source = "consignmentItems",
    target = "carrierBookingReferences")
  @Mapping(target = "amendToTransportDocument", ignore = true)  // FIXME: amendToTransportDocument is obsolete and should be removed!
  @Mapping(target = "areChargesDisplayedOnOriginals", ignore = true)  // Not in the swagger entity
  ShippingInstructionSummaryTO shippingInstructionToShippingInstructionSummary(ShippingInstruction shippingInstruction);

  default String mapShipmentToCarrierBookingReference(ConsignmentItem consignmentItem) {
    return consignmentItem.getCarrierBookingReference();
  }
}
