package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ValueAddedService;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ValueAddedServiceCode;
import org.dcsa.edocumentation.transferobjects.ValueAddedServiceTO;

import java.util.UUID;

@UtilityClass
public class ValueAddedServiceRequestDataFactory {

  public ValueAddedService singleValueAddedServiceRequest() {
    return ValueAddedService.builder()
        .id(UUID.randomUUID())
        .valueAddedServiceCode(ValueAddedServiceCode.SIOT)
        .build();
  }

  public ValueAddedService singleValueAddedServiceRequestWithoutId() {
    return ValueAddedService.builder()
      .valueAddedServiceCode(ValueAddedServiceCode.SIOT)
      .build();
  }

  public ValueAddedServiceTO singleValueAddedServiceRequestTO() {
    return ValueAddedServiceTO.builder()
      .valueAddedServiceCode(org.dcsa.edocumentation.transferobjects.enums.ValueAddedServiceCode.SIOT)
      .build();
  }
}
