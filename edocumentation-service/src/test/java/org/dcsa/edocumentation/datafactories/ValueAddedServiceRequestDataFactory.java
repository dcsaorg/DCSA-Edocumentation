package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedServiceRequest;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ValueAddedServiceCode;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceRequestTO;

import java.util.UUID;

@UtilityClass
public class ValueAddedServiceRequestDataFactory {

  public ValueAddedServiceRequest singleValueAddedServiceRequest() {
    return ValueAddedServiceRequest.builder()
        .id(UUID.randomUUID())
        .valueAddedServiceCode(ValueAddedServiceCode.SIOT)
        .build();
  }

  public ValueAddedServiceRequest singleValueAddedServiceRequestWithoutId() {
    return ValueAddedServiceRequest.builder()
      .valueAddedServiceCode(ValueAddedServiceCode.SIOT)
      .build();
  }

  public ValueAddedServiceRequestTO singleValueAddedServiceRequestTO() {
    return ValueAddedServiceRequestTO.builder()
      .valueAddedServiceCode(org.dcsa.edocumentation.transferobjects.enums.ValueAddedServiceCode.SIOT)
      .build();
  }
}
