package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Reference;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReferenceTypeCode;
import org.dcsa.edocumentation.transferobjects.ReferenceTO;
import org.dcsa.edocumentation.transferobjects.enums.ReferenceType;

import java.util.UUID;

@UtilityClass
public class ReferenceDataFactory {

  public Reference singleReference() {
    return referenceBuilder()
        .referenceID(UUID.randomUUID())
        .build();
  }

  public Reference singleReferenceWithoutId() {
    return referenceBuilder().build();
  }

  private Reference.ReferenceBuilder referenceBuilder() {
    return Reference.builder()
      .type(ReferenceTypeCode.AAO)
      .value("ref value");
  }

  public ReferenceTO singleReferenceTO() {
    return ReferenceTO.builder()
      .type(ReferenceType.AAO)
      .value("ref value")
      .build();
  }
}
