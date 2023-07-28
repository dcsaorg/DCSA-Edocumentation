package org.dcsa.edocumentation.service.mapping;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface EDocumentationMappingConfig {}
