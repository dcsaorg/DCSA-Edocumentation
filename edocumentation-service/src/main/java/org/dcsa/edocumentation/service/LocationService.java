package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.AddressRepository;
import org.dcsa.edocumentation.domain.persistence.FacilityRepository;
import org.dcsa.edocumentation.domain.persistence.repository.LocationRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UnLocationRepository;
import org.dcsa.edocumentation.service.mapping.AddressMapper;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.FacilityLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {
  private final LocationRepository locationRepository;
  private final AddressRepository addressRepository;
  private final FacilityRepository facilityRepository;
  private final UnLocationRepository unLocationRepository;
  private final AddressMapper addressMapper;

  /**
   * Ensures that a location is resolvable. This method only returns null if the input LocationTO is null, if the input
   * is non-null then this method will not return null. Will create Locations and Addresses if no matching Locations
   * or Addresses are found. If a location is not resolvable (i.e. for unknown Facilities or UNLocations) throws
   * an Exception.
   */
  @Transactional
  public Location ensureResolvable(LocationTO locationTO) {
    if (locationTO == null) {
      return null;
    } else if (locationTO instanceof AddressLocationTO addressLocationTO) {
      return ensureResolvable(addressLocationTO);
    } else if (locationTO instanceof UNLocationLocationTO unLocationLocationTO) {
      return ensureResolvable(unLocationLocationTO);
    } else if (locationTO instanceof FacilityLocationTO facilityLocationTO) {
      return ensureResolvable(facilityLocationTO);
    } else {
      throw ConcreteRequestErrorMessageException.internalServerError("Unable to resolve location of type "
        + locationTO.getClass().getSimpleName());
    }
  }

  private Location ensureResolvable(AddressLocationTO locationTO) {
    Address mappedAddress = addressMapper.toDAO(locationTO.address());
    ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll().withIncludeNullValues().withIgnorePaths("id");

    return addressRepository.findAll(Example.of(mappedAddress, exampleMatcher)).stream()
      .findFirst()
      .map(address ->
        locationRepository.findByLocationNameAndAddress(locationTO.locationName(), address).stream()
          .findFirst()
          .orElseGet(() ->
            locationRepository.save(Location.builder()
              .locationName(locationTO.locationName())
              .address(address)
              .build())
          )
      )
      .orElseGet(() ->
        locationRepository.save(Location.builder()
          .locationName(locationTO.locationName())
          .address(addressRepository.save(mappedAddress))
          .build())
      );
  }

  private Location ensureResolvable(UNLocationLocationTO locationTO) {
    unLocationRepository.findById(locationTO.UNLocationCode())
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No UNLocation found for UNLocationCode = '" + locationTO.UNLocationCode() + "'"));

    return locationRepository.findByLocationNameAndUNLocationCode(locationTO.locationName(), locationTO.UNLocationCode()).stream()
      .findFirst()
      .orElseGet(() ->
        locationRepository.save(Location.builder()
          .locationName(locationTO.locationName())
          .UNLocationCode(locationTO.UNLocationCode())
          .build())
      );
  }

  private Location ensureResolvable(FacilityLocationTO locationTO) {
    Facility facility = (switch (locationTO.facilityCodeListProvider()) {
      case SMDG -> facilityRepository.findByUNLocationCodeAndFacilitySMDGCode(locationTO.UNLocationCode(), locationTO.facilityCode());
      case BIC -> facilityRepository.findByUNLocationCodeAndFacilityBICCode(locationTO.UNLocationCode(), locationTO.facilityCode());
    })
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No facility found for UNLocationCode = '" + locationTO.UNLocationCode()
          + "' and facility" + locationTO.facilityCodeListProvider() + "Code = '" + locationTO.facilityCode() + "'"));

    return locationRepository.findByLocationNameAndFacilityAndUNLocationCode(locationTO.locationName(), facility, locationTO.UNLocationCode()).stream()
      .findFirst()
      .orElseGet(() ->
        locationRepository.save(Location.builder()
          .locationName(locationTO.locationName())
          .facility(facility)
          .UNLocationCode(locationTO.UNLocationCode())
          .build())
      );
  }
}
