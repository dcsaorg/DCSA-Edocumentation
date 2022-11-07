package org.dcsa.edocumentation.service.mapping;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dcsa.edocumentation.transferobjects.LocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.AddressLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.FacilityLocationTO;
import org.dcsa.edocumentation.transferobjects.LocationTO.UNLocationLocationTO;
import org.dcsa.edocumentation.transferobjects.enums.FacilityCodeListProvider;
import org.dcsa.skernel.domain.persistence.entity.Facility;
import org.dcsa.skernel.domain.persistence.entity.Location;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class LocationMapper {
  private final AddressMapper addressMapper;

  public LocationTO toDTO(Location location) {
    if (location == null) {
      return null;
    }

    if (location.getAddress() != null) {
      return AddressLocationTO.builder()
        .locationName(location.getLocationName())
        .address(addressMapper.toDTO(location.getAddress()))
        .build();
    } else if (location.getFacility() != null) {
      Facility facility = location.getFacility();
      String facilityCode;
      FacilityCodeListProvider facilityCodeListProvider;
      if (facility.getFacilitySMDGCode() != null) {
        facilityCode = facility.getFacilitySMDGCode();
        facilityCodeListProvider = FacilityCodeListProvider.SMDG;
      } else if (facility.getFacilityBICCode() != null) {
        facilityCode = facility.getFacilityBICCode();
        facilityCodeListProvider = FacilityCodeListProvider.BIC;
      } else {
        throw new IllegalArgumentException("Facility '" + facility.getId()+ "' has neither SMDG code nor BIC code");
      }
      return FacilityLocationTO.builder()
        .locationName(location.getLocationName())
        .UNLocationCode(location.getUNLocationCode())
        .facilityCode(facilityCode)
        .facilityCodeListProvider(facilityCodeListProvider)
        .build();
    } else if (location.getUNLocationCode() != null) {
      return UNLocationLocationTO.builder()
        .locationName(location.getLocationName())
        .UNLocationCode(location.getUNLocationCode())
        .build();
    } else {
      throw new IllegalArgumentException("Location '" + location.getId() + "' has neither address, facility nor unLocation");
    }
  }
}
