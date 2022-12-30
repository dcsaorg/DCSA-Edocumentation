package org.dcsa.edocumentation.service.mapping;

import lombok.SneakyThrows;
import org.dcsa.edocumentation.domain.persistence.entity.DisplayedAddress;
import org.dcsa.skernel.errors.exceptions.ConcreteRequestErrorMessageException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
    Map<Integer, String> addressLineMaps = new HashMap();
    for (Method m : displayedAddress.getClass().getMethods()) {
      if (m.getName().startsWith("getAddressLine") && m.getParameterTypes().length == 0) {

        final String addressLine = (String) m.invoke(displayedAddress);
        if(addressLine == null) {
          continue;
        }
        Integer index = Integer.valueOf(m.getName().substring(m.getName().length() - 1));
        addressLineMaps.put(index, addressLine);
      }
    }
    return Stream.of(addressLineMaps)
      .map(TreeMap::new)
      .map(TreeMap::values)
      .flatMap(Collection::stream)
      .toList();
  }
}
