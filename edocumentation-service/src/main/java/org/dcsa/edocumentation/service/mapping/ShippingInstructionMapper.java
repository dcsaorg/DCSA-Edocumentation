package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  uses = {
    LocationMapper.class,
    DisplayedAddressMapper.class,
    ReferenceMapper.class,
    DocumentPartyMapper.class,
    ConsignmentItemMapper.class
  })
public interface ShippingInstructionMapper {
  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  @Mapping(source = "consignmentItems", target = "consignmentItems", ignore = true)
  ShippingInstruction toDAO(ShippingInstructionTO shippingInstructionTO);

  // TODO: Complete this stub mapping (DDT-1296)
  @Mapping(source = "consignmentItems", target = "consignmentItems")
  ShippingInstructionTO toDTO(ShippingInstruction shippingInstruction);

  ShippingInstructionRefStatusTO toStatusDTO(ShippingInstruction shippingInstruction);
}
