package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;
import org.dcsa.edocumentation.transferobjects.CustomsReferenceTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class
)
public interface CustomsReferenceMapper {
  @Mapping(target = "referenceID", ignore = true)
  @Mapping(target = "utilizedTransportEquipment", ignore = true)
  @Mapping(target = "shippingInstruction", ignore = true)
  @Mapping(target = "consignmentItem", ignore = true)
  @Mapping(target = "cargoItem", ignore = true)
  CustomsReference toDAO(CustomsReferenceTO customsReferenceTO);
}
