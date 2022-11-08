package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReferenceTypeCode;

import java.util.UUID;

@UtilityClass
public class ReferenceDataFactory {

  public Reference singleReference() {
    return Reference.builder()
        .referenceID(UUID.randomUUID())
        .type(ReferenceTypeCode.AAO)
        .value("ref value")
        .build();
  }
}
