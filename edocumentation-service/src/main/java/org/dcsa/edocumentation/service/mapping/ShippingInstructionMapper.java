package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Mapper(componentModel = "spring",
  uses = {
    LocationMapper.class,
    DisplayedAddressMapper.class,
    ReferenceMapper.class,
    DocumentPartyMapper.class,
    ConsignmentItemMapper.class
  })
public abstract class ShippingInstructionMapper {

  @Autowired
  protected UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;

  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  @Mapping(source = "consignmentItems", target = "consignmentItems", ignore = true)
  public abstract ShippingInstruction toDAO(ShippingInstructionTO shippingInstructionTO);

  // TODO: Complete this stub mapping (DDT-1296)
  @Mapping(source = "consignmentItems", target = "utilizedTransportEquipments")
  public abstract ShippingInstructionTO toDTO(ShippingInstruction shippingInstruction);

  protected List<UtilizedTransportEquipmentTO> mapConsignmentItemSetToUtilizedTransportEquipmentTOList(Set<ConsignmentItem> consignmentItemSet) {
    return consignmentItemSet.stream()
      .flatMap(ci -> ci.getCargoItems().stream())
      .map(CargoItem::getUtilizedTransportEquipment)
      .distinct()
      .map(utilizedTransportEquipmentMapper::toDTO)
      .toList();
  }


  public abstract ShippingInstructionRefStatusTO toStatusDTO(ShippingInstruction shippingInstruction);

}
