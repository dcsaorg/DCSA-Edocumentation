package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.skernel.domain.persistence.entity.Location;

import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class ShipmentLocationDataFactory {

  public ShipmentLocation singleShipmentLocation() {
    return ShipmentLocation.builder()
        .id(UUID.randomUUID())
        .location(Location.builder().address(AddressDataFactory.getSingleAddress()).build())
        .shipmentLocationTypeCode(LocationType.OIR)
        .displayedName("Displayed name")
        .eventDateTime(OffsetDateTime.now())
        .build();
  }

}
