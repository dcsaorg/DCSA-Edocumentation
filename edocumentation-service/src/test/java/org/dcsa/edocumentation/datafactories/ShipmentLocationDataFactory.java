package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Booking;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;
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
        .eventDateTime(OffsetDateTime.now())
        .build();
  }

  public ShipmentLocationTO shipmentLocationTO() {
    return shipmentLocationTO(OffsetDateTime.now());
  }

  public ShipmentLocationTO shipmentLocationTO(OffsetDateTime now) {
    return ShipmentLocationTO.builder()
      .location(LocationDataFactory.addressLocationTO())
      .shipmentLocationTypeCode(ShipmentLocationTypeCode.OIR)
      .eventDateTime(now)
      .build();
  }

  public ShipmentLocation shipmentLocation(Booking booking, OffsetDateTime now) {
    return ShipmentLocation.builder()
      .booking(booking)
      .location(LocationDataFactory.addressLocationWithId())
      .shipmentLocationTypeCode(LocationType.OIR)
      .eventDateTime(now)
      .build();
  }
}
