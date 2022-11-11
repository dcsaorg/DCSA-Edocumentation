package org.dcsa.edocumentation.transferobjects.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.enums.FacilityCodeListProvider;

import java.io.IOException;

/**
 * Need a custom deserializer for LocationTO since there is no discriminator and the fields
 * are not unique enough for Jackson to figure it out automagically (spelling intended).
 */
public class LocationTODeserializer extends StdDeserializer<LocationTO> {
  public LocationTODeserializer() {
    super(LocationTO.class);
  }

  @Override
  public LocationTO deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
    CombinedLocation combinedLocation = jp.readValueAs(CombinedLocation.class);
    if (combinedLocation.isAddress()) {
      if (combinedLocation.UNLocationCode != null || combinedLocation.facilityCode != null | combinedLocation.facilityCodeListProvider != null) {
        throw new IllegalArgumentException(
          "Invalid json, cannot deserialize a location containing address together" +
            " with UNLocationCode, facilityCode or facilityCodeListProvider");
      }
      return LocationTO.addressLocationBuilder()
        .locationName(combinedLocation.locationName)
        .address(combinedLocation.address)
        .build();
    } else if (combinedLocation.isFacility()) {
      return LocationTO.facilityLocationBuilder()
        .locationName(combinedLocation.locationName)
        .facilityCode(combinedLocation.facilityCode)
        .facilityCodeListProvider(combinedLocation.facilityCodeListProvider)
        .UNLocationCode(combinedLocation.UNLocationCode())
        .build();
    } else if (combinedLocation.isUNLocation()){
      return LocationTO.unLocationLocationBuilder()
        .locationName(combinedLocation.locationName)
        .UNLocationCode(combinedLocation.UNLocationCode())
        .build();
    } else {
      throw new IllegalArgumentException(
        "Invalid json, cannot deserialize a location with neither address " +
          " UNLocationCode nor facility{Code,CodeListProvider}");
    }
  }

  private record CombinedLocation(
    String locationName,
    @JsonProperty("UNLocationCode") String UNLocationCode,
    String facilityCode,
    FacilityCodeListProvider facilityCodeListProvider,
    AddressTO address
  ) {
    boolean isAddress() { return address != null; }
    boolean isFacility() { return facilityCodeListProvider != null && facilityCode != null; }
    boolean isUNLocation() { return UNLocationCode != null && !isFacility(); }
  }
}
