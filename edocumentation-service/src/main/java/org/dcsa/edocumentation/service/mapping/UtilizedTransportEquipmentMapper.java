package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.UtilizedTransportEquipmentTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    ActiveReeferSettingsMapper.class,
    EquipmentMapper.class,
    SealMapper.class,
    CustomsReferenceMapper.class,
    ReferenceMapper.class,
})
public interface UtilizedTransportEquipmentMapper {

  @Mapping(source = "utilizedTransportEquipmentTO.isShipperOwned", target = "isShipperOwned")
  @Mapping(source = "equipment", target = "equipment")
  @Mapping(target = "id", ignore = true)
  UtilizedTransportEquipment toDAO(UtilizedTransportEquipmentTO utilizedTransportEquipmentTO, Equipment equipment);

  @Mapping(target = "equipmentReference", expression = "java(null)")  // FIXME: In TD, always absent. In other cases, conditional on "isSOC"!
  @Mapping(target = "references", ignore = true)  // FIXME: Missing on the DAO
  // FIXME: I think these need to be mappined depending on the case (input/request vs. output/response mapping)
  @Mapping(target = "cargoGrossVolume", ignore = true)
  @Mapping(target = "cargoGrossVolumeUnit", ignore = true)
  @Mapping(target = "numberOfPackages", ignore = true)
  @Mapping(target = "isNonOperatingReefer", ignore = true)
  @Mapping(target = "activeReeferSettings", ignore = true)
  UtilizedTransportEquipmentTO toDTO(UtilizedTransportEquipment utilizedTransportEquipment);

}
