package org.dcsa.edocumentation.service.mapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.decoupled.entity.ShippingInstruction;
import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionRefStatusTO;
import org.dcsa.edocumentation.transferobjects.ShippingInstructionTO;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;
import org.dcsa.edocumentation.transferobjects.enums.TransportDocumentTypeCode;
import org.dcsa.skernel.infrastructure.services.mapping.LocationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Mapper(componentModel = "spring",
  uses = {
    LocationMapper.class,
    DisplayedAddressMapper.class,
    ConsignmentItemMapper.class
  })
public abstract class ShippingInstructionMapper {
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  protected UtilizedTransportEquipmentMapper utilizedTransportEquipmentMapper;

  public abstract ShippingInstructionRefStatusTO toStatusDTO(ShippingInstructionTO shippingInstructionTO);
  public abstract org.dcsa.edocumentation.domain.persistence.entity.enums.EblDocumentStatus toDAO(EblDocumentStatus documentStatus);
  public abstract org.dcsa.edocumentation.domain.persistence.entity.enums.TransportDocumentTypeCode toDAO(TransportDocumentTypeCode transportDocumentTypeCode);

  @SneakyThrows
  public ShippingInstructionTO toDTO(ShippingInstruction shippingInstruction) {
    return objectMapper.readValue(shippingInstruction.getContent(), ShippingInstructionTO.class);
  }

  @SneakyThrows
  public ShippingInstruction toDAO(ShippingInstructionTO shippingInstructionTO) {
    return ShippingInstruction.builder()
      .shippingInstructionReference(shippingInstructionTO.shippingInstructionReference())
      .documentStatus(toDAO(shippingInstructionTO.documentStatus()))
      .transportDocumentTypeCode(toDAO(shippingInstructionTO.transportDocumentTypeCode()))
      .content(objectMapper.writeValueAsString(shippingInstructionTO))
      .createdDateTime(shippingInstructionTO.shippingInstructionCreatedDateTime())
      .updatedDateTime(shippingInstructionTO.shippingInstructionUpdatedDateTime())
      .build();
  }

  @Mapping(source = "consignmentItems", target = "utilizedTransportEquipments")
  public abstract ShippingInstructionTO toDTO(org.dcsa.edocumentation.domain.persistence.entity.ShippingInstruction shippingInstruction);

  protected List<UtilizedTransportEquipmentTO> mapConsignmentItemSetToUtilizedTransportEquipmentTOList(Set<ConsignmentItem> consignmentItemSet) {
    return consignmentItemSet.stream()
      .flatMap(ci -> ci.getCargoItems().stream())
      .map(CargoItem::getUtilizedTransportEquipment)
      .distinct()
      .map(utilizedTransportEquipmentMapper::toDTO)
      .toList();
  }
}
