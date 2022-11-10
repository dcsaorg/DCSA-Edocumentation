package org.dcsa.edocumentation.service;

import lombok.RequiredArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.FacilityRepository;
import org.dcsa.edocumentation.domain.persistence.repository.LocationRepository;
import org.dcsa.edocumentation.domain.persistence.repository.UnLocationRepository;
import org.dcsa.edocumentation.service.util.EnsureResolvable;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.FacilityLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.skernel.domain.persistence.entity.Address;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class LocationService extends EnsureResolvable<LocationTO, Location> {
  private final AddressService addressService;
  private final LocationRepository locationRepository;
  private final FacilityRepository facilityRepository;
  private final UnLocationRepository unLocationRepository;

  /**
   * Ensures that a location is resolvable. Will create Locations and Addresses if no matching Locations
   * or Addresses are found. If a location is not resolvable (i.e. for unknown Facilities or UNLocations) throws
   * an Exception.
   * If the input is null will return the result of calling the mapper with (null, false).
   */
  @Override
  @Transactional
  public <C> C ensureResolvable(LocationTO locationTO, BiFunction<Location, Boolean, C> mapper) {
    if (locationTO == null) {
      return mapper.apply(null, false);
    } else if (locationTO instanceof AddressLocationTO addressLocationTO) {
      return ensureResolvable(addressLocationTO, mapper);
    } else if (locationTO instanceof UNLocationLocationTO unLocationLocationTO) {
      return ensureResolvable(unLocationLocationTO, mapper);
    } else if (locationTO instanceof FacilityLocationTO facilityLocationTO) {
      return ensureResolvable(facilityLocationTO, mapper);
    } else {
      throw ConcreteRequestErrorMessageException.internalServerError("Unable to resolve location of type "
        + locationTO.getClass().getSimpleName());
    }
  }

  private <C> C ensureResolvable(AddressLocationTO locationTO, BiFunction<Location, Boolean, C> mapper) {
    Function<Address, Location> creator = address -> locationRepository.save(Location.builder()
      .locationName(locationTO.locationName())
      .address(address)
      .build());

    return addressService.ensureResolvable(locationTO.address(), (Address address, Boolean isAddressNew) -> {
      if (isAddressNew) {
        return mapper.apply(creator.apply(address), true);
      } else {
        return ensureResolvable(
          locationRepository.findByLocationNameAndAddress(locationTO.locationName(), address),
          () -> creator.apply(address),
          mapper
        );
      }
    });
  }

  private <C> C ensureResolvable(UNLocationLocationTO locationTO, BiFunction<Location, Boolean, C> mapper) {
    unLocationRepository.findById(locationTO.UNLocationCode())
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No UNLocation found for UNLocationCode = '" + locationTO.UNLocationCode() + "'"));

    return ensureResolvable(
      locationRepository.findByLocationNameAndUNLocationCode(locationTO.locationName(), locationTO.UNLocationCode()),
      () -> locationRepository.save(Location.builder()
        .locationName(locationTO.locationName())
        .UNLocationCode(locationTO.UNLocationCode())
        .build()),
      mapper);
  }

  private <C> C ensureResolvable(FacilityLocationTO locationTO, BiFunction<Location, Boolean, C> mapper) {
    Facility facility = (switch (locationTO.facilityCodeListProvider()) {
      case SMDG -> facilityRepository.findByUNLocationCodeAndFacilitySMDGCode(locationTO.UNLocationCode(), locationTO.facilityCode());
      case BIC -> facilityRepository.findByUNLocationCodeAndFacilityBICCode(locationTO.UNLocationCode(), locationTO.facilityCode());
    })
      .orElseThrow(() -> ConcreteRequestErrorMessageException.notFound(
        "No facility found for UNLocationCode = '" + locationTO.UNLocationCode()
          + "' and facility" + locationTO.facilityCodeListProvider() + "Code = '" + locationTO.facilityCode() + "'"));

    return ensureResolvable(
      locationRepository.findByLocationNameAndFacilityAndUNLocationCode(locationTO.locationName(), facility, locationTO.UNLocationCode()),
      () -> locationRepository.save(Location.builder()
        .locationName(locationTO.locationName())
        .facility(facility)
        .UNLocationCode(locationTO.UNLocationCode())
        .build()),
      mapper
      );
  }
}
