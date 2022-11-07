package org.dcsa.edocumentation.service;

import org.dcsa.edocumentation.datafactories.AddressDataFactory;
import org.dcsa.edocumentation.datafactories.LocationDataFactory;
import org.dcsa.edocumentation.domain.persistence.AddressRepository;
import org.dcsa.edocumentation.domain.persistence.FacilityRepository;
import org.dcsa.edocumentation.domain.persistence.entity.UnLocation;
import org.dcsa.edocumentation.domain.persistence.repository.LocationRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UnLocationRepository;
import org.dcsa.edocumentation.service.mapping.AddressMapper;
import org.dcsa.edocumentation.transferobjects.AddressTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.FacilityLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {
  @Mock private LocationRepository locationRepository;
  @Mock private AddressRepository addressRepository;
  @Mock private FacilityRepository facilityRepository;
  @Mock private UnLocationRepository unLocationRepository;
  @Mock private AddressMapper addressMapper;

  @InjectMocks private LocationService locationService;

  @BeforeEach
  public void resetMocks() {
    reset(locationRepository, addressRepository, facilityRepository, unLocationRepository, addressMapper);
  }

  @Test
  public void testNull() {
    assertNull(locationService.ensureResolvable(null));
  }

  @Test
  public void testAddressLocation_AddressNotFound() {
    // Setup
    AddressLocationTO locationTO = LocationDataFactory.addressLocationTO();
    Location location = LocationDataFactory.addressLocationWithId();

    when(addressMapper.toDAO(any(AddressTO.class))).thenReturn(AddressDataFactory.addressWithoutId());
    when(addressRepository.findAll(any(Example.class))).thenReturn(Collections.emptyList());
    when(addressRepository.save(any(Address.class))).thenReturn(location.getAddress());
    when(locationRepository.save(any(Location.class))).thenReturn(location);

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(addressMapper).toDAO(locationTO.address());
    verify(addressRepository).findAll(any(Example.class));
    verify(locationRepository, never()).findByLocationNameAndAddress(anyString(), any(Address.class));
    verify(addressRepository).save(AddressDataFactory.addressWithoutId());
    verify(locationRepository).save(LocationDataFactory.addressLocationWithoutId());
  }

  @Test
  public void testAddressLocation_AddressFound_LocationNotFound() {
    // Setup
    AddressLocationTO locationTO = LocationDataFactory.addressLocationTO();
    Location location = LocationDataFactory.addressLocationWithId();

    when(addressMapper.toDAO(any(AddressTO.class))).thenReturn(AddressDataFactory.addressWithoutId());
    when(addressRepository.findAll(any(Example.class))).thenReturn(List.of(location.getAddress()));
    when(locationRepository.findByLocationNameAndAddress(anyString(), any(Address.class))).thenReturn(Collections.emptyList());
    when(locationRepository.save(any(Location.class))).thenReturn(location);

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(addressMapper).toDAO(locationTO.address());
    verify(addressRepository).findAll(any(Example.class));
    verify(locationRepository).findByLocationNameAndAddress(locationTO.locationName(), location.getAddress());
    verify(addressRepository, never()).save(any(Address.class));
    verify(locationRepository).save(LocationDataFactory.addressLocationWithoutId());
  }

  @Test
  public void testAddressLocation_AddressFound_LocationFound() {
    // Setup
    AddressLocationTO locationTO = LocationDataFactory.addressLocationTO();
    Location location = LocationDataFactory.addressLocationWithId();

    when(addressMapper.toDAO(any(AddressTO.class))).thenReturn(AddressDataFactory.addressWithoutId());
    when(addressRepository.findAll(any(Example.class))).thenReturn(List.of(location.getAddress()));
    when(locationRepository.findByLocationNameAndAddress(anyString(), any(Address.class))).thenReturn(List.of(location));

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(addressMapper).toDAO(locationTO.address());
    verify(addressRepository).findAll(any(Example.class));
    verify(locationRepository).findByLocationNameAndAddress(locationTO.locationName(), location.getAddress());
    verify(addressRepository, never()).save(any(Address.class));
    verify(locationRepository, never()).save(any(Location.class));
  }

  @Test
  public void testUNLocation_UNLocationNotFound() {
    // Setup
    UNLocationLocationTO locationTO = LocationDataFactory.unLocationLocationTO();
    when(unLocationRepository.findById(anyString())).thenReturn(Optional.empty());

    // Execute/Verify
    NotFoundException exception = assertThrows(NotFoundException.class, () -> locationService.ensureResolvable(locationTO));
    assertEquals("No UNLocation found for UNLocationCode = 'NLRTM'", exception.getMessage());
  }

  @Test
  public void testUNLocation_UNLocationFound_LocationNotFound() {
    // Setup
    UNLocationLocationTO locationTO = LocationDataFactory.unLocationLocationTO();
    Location location = LocationDataFactory.unLocationLocationWithId();

    when(unLocationRepository.findById(anyString())).thenReturn(Optional.of(UnLocation.builder().build()));
    when(locationRepository.findByLocationNameAndUNLocationCode(anyString(), anyString())).thenReturn(Collections.emptyList());
    when(locationRepository.save(any(Location.class))).thenReturn(location);

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(unLocationRepository).findById(locationTO.UNLocationCode());
    verify(locationRepository).findByLocationNameAndUNLocationCode(locationTO.locationName(), locationTO.UNLocationCode());
    verify(locationRepository).save(LocationDataFactory.unLocationLocationWithoutId());
  }

  @Test
  public void testUNLocation_UNLocationFound_LocationFound() {
    // Setup
    UNLocationLocationTO locationTO = LocationDataFactory.unLocationLocationTO();
    Location location = LocationDataFactory.unLocationLocationWithId();

    when(unLocationRepository.findById(anyString())).thenReturn(Optional.of(UnLocation.builder().build()));
    when(locationRepository.findByLocationNameAndUNLocationCode(anyString(), anyString())).thenReturn(List.of(location));

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(unLocationRepository).findById(locationTO.UNLocationCode());
    verify(locationRepository).findByLocationNameAndUNLocationCode(locationTO.locationName(), locationTO.UNLocationCode());
    verify(locationRepository, never()).save(any(Location.class));
  }

  @Test
  public void testFacilityLocation_FacilityNotFound() {
    // Setup
    FacilityLocationTO locationTO = LocationDataFactory.facilityLocationTO();
    when(facilityRepository.findByUNLocationCodeAndFacilitySMDGCode(anyString(), anyString())).thenReturn(Optional.empty());

    // Execute/Verify
    NotFoundException exception = assertThrows(NotFoundException.class, () -> locationService.ensureResolvable(locationTO));
    assertEquals("No facility found for UNLocationCode = 'AUSYD' and facilitySMDGCode = 'ASLPB'", exception.getMessage());
  }

  @Test
  public void testFacilityLocation_FacilityFound_LocationNotFound() {
    // Setup
    FacilityLocationTO locationTO = LocationDataFactory.facilityLocationTO();
    Location location = LocationDataFactory.facilityLocationWithId();

    when(facilityRepository.findByUNLocationCodeAndFacilitySMDGCode(anyString(), anyString())).thenReturn(Optional.of(location.getFacility()));
    when(locationRepository.findByLocationNameAndFacilityAndUNLocationCode(anyString(), any(Facility.class), anyString())).thenReturn(Collections.emptyList());
    when(locationRepository.save(any(Location.class))).thenReturn(location);

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(facilityRepository).findByUNLocationCodeAndFacilitySMDGCode(locationTO.UNLocationCode(), locationTO.facilityCode());
    verify(locationRepository).findByLocationNameAndFacilityAndUNLocationCode(locationTO.locationName(), location.getFacility(), locationTO.UNLocationCode());
    verify(locationRepository).save(LocationDataFactory.facilityLocationWithoutId());
  }

  @Test
  public void testFacilityLocation_FacilityFound_LocationFound() {
    // Setup
    FacilityLocationTO locationTO = LocationDataFactory.facilityLocationTO();
    Location location = LocationDataFactory.facilityLocationWithId();

    when(facilityRepository.findByUNLocationCodeAndFacilitySMDGCode(anyString(), anyString())).thenReturn(Optional.of(location.getFacility()));
    when(locationRepository.findByLocationNameAndFacilityAndUNLocationCode(anyString(), any(Facility.class), anyString())).thenReturn(List.of(location));

    // Execute
    Location actual = locationService.ensureResolvable(locationTO);

    // Verify
    assertEquals(location, actual);
    verify(facilityRepository).findByUNLocationCodeAndFacilitySMDGCode(locationTO.UNLocationCode(), locationTO.facilityCode());
    verify(locationRepository).findByLocationNameAndFacilityAndUNLocationCode(locationTO.locationName(), location.getFacility(), locationTO.UNLocationCode());
    verify(locationRepository, never()).save(any(Location.class));
  }
}
