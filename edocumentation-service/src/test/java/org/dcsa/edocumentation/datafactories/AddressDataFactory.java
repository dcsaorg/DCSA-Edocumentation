package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.Address;
import org.dcsa.edocumentation.transferobjects.AddressTO;

import java.util.UUID;

@UtilityClass
public class AddressDataFactory {
  private static String name = "Asseco DK";
  private static String street = "Kronprinsessegade";
  private static String streetNumber = "54";
  private static String floor = "5. sal";
  private static String postCode = "1306";
  private static String city = "København";
  private static String country = "Denmark";

  public Address getSingleAddress() {
    return Address.builder()
      .id(UUID.randomUUID())
      .name("DCSA Address")
      .country("Netherlands")
      .city("Amsterdam")
      .stateRegion("Noord-Holland")
      .postCode("1077 ZX")
      .street("Strawingskylaan")
      .streetNumber("4117")
      .floor("6")
      .build();
  }

  public static AddressTO addressTO() {
    return AddressTO.builder()
      .name(name)
      .street(street)
      .streetNumber(streetNumber)
      .floor(floor)
      .postCode(postCode)
      .city(city)
      .country(country)
      .build();
  }

  public static Address addressWithoutId() {
    return addressBuilder().build();
  }

  public static Address addressWithId() {
    return addressBuilder()
      .id(UUID.fromString("5a68204b-3be7-4e93-9bbc-9feda5c47018"))
      .build();
  }

  private static Address.AddressBuilder addressBuilder() {
    return Address.builder()
      .name(name)
      .street(street)
      .streetNumber(streetNumber)
      .floor(floor)
      .postCode(postCode)
      .city(city)
      .country(country)
      ;
  }
}
