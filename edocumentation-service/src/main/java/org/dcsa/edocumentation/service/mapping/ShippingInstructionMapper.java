package org.dcsa.edocumentation.service.mapping;

import java.util.List;
import java.util.Set;
import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    LocationMapper.class,
    DisplayedAddressMapper.class,
    ReferenceMapper.class,
    DocumentPartyMapper.class,
    ConsignmentItemMapper.class,
    DisplayedAddressMapper.class,
    AddressMapper.class,
    CustomsReferenceMapper.class,
  })
public abstract class ShippingInstructionMapper {

  @Autowired
  protected UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;

  @Mapping(source = "documentParties", target = "documentParties", ignore = true)
  @Mapping(source = "consignmentItems", target = "consignmentItems", ignore = true)
  @Mapping(target = "amendmentToTransportDocument", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME!
  // Internal details; should not be mapped
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isNew", ignore = true)
  @Mapping(target = "validUntil", ignore = true)
  public abstract ShippingInstruction toDAO(ShippingInstructionTO shippingInstructionTO);

  // TODO: Complete this stub mapping (DDT-1296)
  @Mapping(source = "consignmentItems", target = "utilizedTransportEquipments")
  @Mapping(target = "amendmentToTransportDocument", ignore = true)  // FIXME: We should be mapping this.
  @Mapping(target = "areChargesDisplayedOnOriginals", ignore = true)  // FIXME: DAO/TO should be aligned here
  @Mapping(target = "areChargesDisplayedOnCopies", ignore = true)  // FIXME: DAO/TO should be aligned here
  public abstract ShippingInstructionTO toDTO(ShippingInstruction shippingInstruction);

  protected List<UtilizedTransportEquipmentTO> mapConsignmentItemListToUtilizedTransportEquipmentTOList(List<ConsignmentItem> consignmentItems) {
    return consignmentItems.stream()
      .flatMap(ci -> ci.getCargoItems().stream())
      .map(CargoItem::getUtilizedTransportEquipment)
      .distinct()
      .map(utilizedTransportEquipmentMapper::toDTO)
      .toList();
  }


  public abstract ShippingInstructionRefStatusTO toStatusDTO(ShippingInstruction shippingInstruction);

}
