package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.datafactories.FacilityDataFactory;
import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.FacilityLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LocationMapperTest {
  @Spy private AddressMapper addressMapper = Mappers.getMapper(AddressMapper.class);
  @InjectMocks private LocationMapper locationMapper;

  @Test
  public void testNull() {
    assertNull(locationMapper.toDTO(null));
  }

  @Test
  public void testAddressLocationMapping() {
    // Setup
    Location location = LocationDataFactory.addressLocationWithId();

    // Execute
    LocationTO actual = locationMapper.toDTO(location);

    // Verify
    assertInstanceOf(AddressLocationTO.class, actual);
    assertEquals(LocationDataFactory.addressLocationTO(), actual);
  }

  @Test
  public void testFacilityLocationMapping() {
    // Setup
    Location location = LocationDataFactory.facilityLocationWithId();

    // Execute
    LocationTO actual = locationMapper.toDTO(location);

    // Verify
    assertInstanceOf(FacilityLocationTO.class, actual);
    assertEquals(LocationDataFactory.facilityLocationTO(), actual);
  }

  @Test
  public void testUNLocationLocationMapping() {
    // Setup
    Location location = LocationDataFactory.unLocationLocationWithId();

    // Execute
    LocationTO actual = locationMapper.toDTO(location);

    // Verify
    assertInstanceOf(UNLocationLocationTO.class, actual);
    assertEquals(LocationDataFactory.unLocationLocationTO(), actual);
  }

  @Test
  public void testEmptyLocation() {
    // Setup
    Location location = Location.builder().build();

    // Execute/Verify
    assertThrows(IllegalArgumentException.class, () -> locationMapper.toDTO(location));
  }

  @Test
  public void testBadFacilityLocation() {
    // Setup
    Location location =Location.builder()
      .locationName(FacilityDataFactory.NAME)
      .UNLocationCode(FacilityDataFactory.UNLOCATION_CODE)
      .facility(Facility.builder()
        .id(UUID.fromString("8a92fb86-000d-498a-89e2-c3422760d018"))
        .facilityName("Bad facility")
        .UNLocationCode("DEHA")
        .build())
      .build();

    // Execute/Verify
    assertThrows(IllegalArgumentException.class, () -> locationMapper.toDTO(location));
  }
}
