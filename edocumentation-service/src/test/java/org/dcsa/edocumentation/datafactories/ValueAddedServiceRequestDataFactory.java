package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedServiceRequest;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ValueAddedServiceCode;

import java.util.UUID;

@UtilityClass
public class ValueAddedServiceRequestDataFactory {

  public ValueAddedServiceRequest singleValueAddedServiceRequest() {
    return ValueAddedServiceRequest.builder()
        .id(UUID.randomUUID())
        .valueAddedServiceCode(ValueAddedServiceCode.SIOT)
        .build();
  }
}
