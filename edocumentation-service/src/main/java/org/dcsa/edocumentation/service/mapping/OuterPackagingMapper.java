package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.OuterPackaging;
import org.dcsa.edocumentation.transferobjects.OuterPackagingTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = EDocumentationMappingConfig.class)
public interface OuterPackagingMapper {

  @Mapping(target = "id", ignore = true)
  OuterPackaging toDAO(OuterPackagingTO outerPackagingTO);

}
