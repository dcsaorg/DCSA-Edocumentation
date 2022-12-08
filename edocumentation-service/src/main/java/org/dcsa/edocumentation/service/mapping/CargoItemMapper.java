package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.Shipment;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CargoItemMapper {

  CargoItem toDAO(CargoItemTO cargoItemTO);


  default String mapUtilizedTransportEquipmentToEquipmentReference(UtilizedTransportEquipment utilizedTransportEquipment) {
    return utilizedTransportEquipment.getEquipment().getEquipmentReference();
  }

  @Mapping(source = "utilizedTransportEquipment", target = "equipmentReference")
  CargoItemTO toDTO(CargoItem cargoItem);
}
