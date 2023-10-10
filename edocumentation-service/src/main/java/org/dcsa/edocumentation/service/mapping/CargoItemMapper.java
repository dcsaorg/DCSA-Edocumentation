package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.CargoItem;
import org.dcsa.edocumentation.domain.persistence.entity.UtilizedTransportEquipment;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
  componentModel = "spring",
  config = EDocumentationMappingConfig.class,
  uses = {
    CustomsReferenceMapper.class
  },
  unmappedTargetPolicy = ReportingPolicy.WARN  // FIXME: Remove this line when we are ready for ERROR.
)
public interface CargoItemMapper {

  @Mapping(target = "id", ignore = true)
  CargoItem toDAO(CargoItemTO cargoItemTO);


  default String mapUtilizedTransportEquipmentToEquipmentReference(UtilizedTransportEquipment utilizedTransportEquipment) {
    return utilizedTransportEquipment.getEquipment().getEquipmentReference();
  }

  @Mapping(source = "utilizedTransportEquipment", target = "equipmentReference")
  CargoItemTO toDTO(CargoItem cargoItem);
}
