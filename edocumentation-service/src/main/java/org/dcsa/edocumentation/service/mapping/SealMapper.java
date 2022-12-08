package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.Seal;
import org.dcsa.edocumentation.transferobjects.SealTO;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SealMapper {

  Seal toDAO(SealTO sealTO, UUID utilizedTransportEquipmentID);

  SealTO toDTO(Seal seal);
}
