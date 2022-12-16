package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Service;

import java.util.UUID;

@UtilityClass
public class ServiceDataFactory {
  public static Service service() {
    return Service.builder()
      .id(UUID.fromString("498531b8-d46a-4f8f-95ef-fd661936eb54"))
      .universalServiceReference("universalServiceReference")
      .carrierServiceCode("carrierServiceCode")
      .build();
  }
}
