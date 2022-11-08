package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class DisplayedAddressMapper {

  List<String> toDTO(Set<DisplayedAddress> displayedAddresses) {
    return displayedAddresses.stream()
        .sorted(Comparator.comparingInt(DisplayedAddress::getAddressLineNumber))
        .map(DisplayedAddress::getAddressLine)
        .toList();
  }
}
