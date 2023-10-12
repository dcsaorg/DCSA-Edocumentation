package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    ReferenceMapper.class,
    CargoItemMapper.class,
    CustomsReferenceMapper.class,
  }
)
public interface ConsignmentItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "shippingInstruction", ignore = true)
  @Mapping(target = "shipment", ignore = true)
  @Mapping(target = "commodity", ignore = true)
  ConsignmentItem toDAO(ConsignmentItemTO consignmentItemTO);

  @Mapping(target = "weight", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME
  @Mapping(target = "weightUnit", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME
  @Mapping(target = "volume", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME
  @Mapping(target = "volumeUnit", ignore = true)  // FIXME: Align DAO/TD or verify it is not necessary and remove FIXME
  ConsignmentItemTO toDTO(ConsignmentItem consignmentItem);
}
