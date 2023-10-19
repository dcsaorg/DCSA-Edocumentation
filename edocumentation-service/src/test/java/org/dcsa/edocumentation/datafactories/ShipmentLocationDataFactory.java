package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.BookingRequest;
import org.dcsa.edocumentation.domain.persistence.entity.Location;
import org.dcsa.edocumentation.domain.persistence.entity.ShipmentLocation;
import org.dcsa.edocumentation.domain.persistence.entity.enums.LocationType;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.enums.ShipmentLocationTypeCode;

import java.time.OffsetDateTime;
import java.util.UUID;

@UtilityClass
public class ShipmentLocationDataFactory {

  public ShipmentLocation singleShipmentLocation() {
    return ShipmentLocation.builder()
        .id(UUID.randomUUID())
        .location(Location.builder().address(AddressDataFactory.getSingleAddress()).build())
        .shipmentLocationTypeCode(LocationType.OIR.name())
        .eventDateTime(OffsetDateTime.now())
        .build();
  }

  public ShipmentLocationTO shipmentLocationTO() {
    return shipmentLocationTO(OffsetDateTime.now());
  }

  public ShipmentLocationTO shipmentLocationTO(OffsetDateTime now) {
    return ShipmentLocationTO.builder()
      .location(LocationDataFactory.addressLocationTO())
      .shipmentLocationTypeCode(ShipmentLocationTypeCode.OIR.name())
      .eventDateTime(now)
      .build();
  }

  public ShipmentLocation shipmentLocation(BookingRequest bookingRequest, OffsetDateTime now) {
    return ShipmentLocation.builder()
      .bookingRequest(bookingRequest)
      .location(LocationDataFactory.addressLocationWithId())
      .shipmentLocationTypeCode(LocationType.OIR.name())
      .eventDateTime(now)
      .build();
  }
}
