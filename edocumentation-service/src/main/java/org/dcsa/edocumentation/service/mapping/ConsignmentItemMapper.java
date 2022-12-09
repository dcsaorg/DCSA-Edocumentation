package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Commodity;
import org.dcsa.edocumentation.domain.persistence.entity.ConsignmentItem;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
  ReferenceMapper.class,
  CargoItemMapper.class,
})
public interface ConsignmentItemMapper {

  @Mapping(source = "cargoItems", target = "cargoItems", ignore = true)
  ConsignmentItem toDAO(ConsignmentItemTO consignmentItemTO);

  default String mapShipmentToCarrierBookingReference(Shipment shipment) {
    return shipment.getCarrierBookingReference();
  }

  default String mapCommodityToHsCode(Commodity commodity) {
    return commodity.getHsCode();
  }


  @Mapping(source = "shipment", target = "carrierBookingReference")
  @Mapping(source = "commodity", target = "hsCode")
  ConsignmentItemTO toDTO(ConsignmentItem consignmentItem);
}
