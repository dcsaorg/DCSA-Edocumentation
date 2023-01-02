package org.dcsa.edocumentation.service.mapping;

import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class DisplayedAddressMapper {

  public List<String> toDTO(DisplayedAddress displayedAddresses) {
    if(displayedAddresses == null) {
      return Collections.emptyList();
    }

    return getAddressLines(displayedAddresses);
  }

  public DisplayedAddress toDAO(List<String> displayedAddresses) {

    if(displayedAddresses == null || displayedAddresses.isEmpty()) {
      return null;
    }

    DisplayedAddress.DisplayedAddressBuilder builder = DisplayedAddress.builder();

    IntStream.range(0, displayedAddresses.size())
      .forEach(index -> setAddressLine(index, displayedAddresses.get(index), builder));

    return builder.build();
  }

  private DisplayedAddress.DisplayedAddressBuilder setAddressLine(Integer index, String addressLine, DisplayedAddress.DisplayedAddressBuilder builder) {
    return switch (index + 1) {
      case 1 -> builder.addressLine1(addressLine);
      case 2 -> builder.addressLine2(addressLine);
      case 3 -> builder.addressLine3(addressLine);
      case 4 -> builder.addressLine4(addressLine);
      case 5 -> builder.addressLine5(addressLine);
      default -> throw ConcreteRequestErrorMessageException.invalidInput("Display Address must be max five lines.");
    };
  }

  @SneakyThrows
  private List<String> getAddressLines(DisplayedAddress displayedAddress) {
    List<String> displayedAddresses = new ArrayList<>();
    if(displayedAddress.getAddressLine1() != null) {
      displayedAddresses.add(displayedAddress.getAddressLine1());
    }
    if(displayedAddress.getAddressLine2() != null) {
      displayedAddresses.add(displayedAddress.getAddressLine2());
    }
    if(displayedAddress.getAddressLine3() != null) {
      displayedAddresses.add(displayedAddress.getAddressLine3());
    }
    if(displayedAddress.getAddressLine4() != null) {
      displayedAddresses.add(displayedAddress.getAddressLine4());
    }
    if(displayedAddress.getAddressLine5() != null) {
      displayedAddresses.add(displayedAddress.getAddressLine5());
    }

    return displayedAddresses;
  }
}
