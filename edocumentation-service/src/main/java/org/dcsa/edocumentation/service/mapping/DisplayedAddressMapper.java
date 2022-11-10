package org.dcsa.edocumentation.service.mapping;

import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.edocumentation.domain.persistence.entity.DocumentParty;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class DisplayedAddressMapper {

  List<String> toDTO(Set<DisplayedAddress> displayedAddresses) {
    return displayedAddresses.stream()
        .sorted(Comparator.comparingInt(DisplayedAddress::getAddressLineNumber))
        .map(DisplayedAddress::getAddressLine)
        .toList();
  }

  public Set<DisplayedAddress> toDAO(List<String> displayedAddresses, DocumentParty documentParty) {
    return IntStream.range(0, displayedAddresses.size())
      .mapToObj(index -> DisplayedAddress.builder()
        .addressLineNumber(index)
        .addressLine(displayedAddresses.get(index))
        .documentParty(documentParty)
        .build())
      .collect(Collectors.toSet());
  }
}
