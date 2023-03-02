package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
  ActiveReeferSettingsMapper.class,
  EquipmentMapper.class,
  SealMapper.class
})
public interface UtilizedTransportEquipmentMapper {

  @Mapping(source = "utilizedTransportEquipmentTO.isShipperOwned", target = "isShipperOwned")
  @Mapping(source = "equipment", target = "equipment")
  UtilizedTransportEquipment toDAO(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO, Equipment equipment);

  UtilizedTransportEquipmentTO toDTO(UtilizedTransportEquipment utilizedTransportEquipment);
}
