package org.dcsa.edocumentation.transferobjects.validation;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
class MultiStringKeySet {
  private final Set<String> values = new LinkedHashSet<>();

  @Min(1)
  private final int keyCount;

  public boolean add(String ... k) {
    return values.add(mapToSingleKey(Arrays.asList(k)));
  }

  public boolean add(List<String> k) {
    return values.add(mapToSingleKey(k));
  }

  public boolean contains(String ... k) {
    return values.contains(mapToSingleKey(Arrays.asList(k)));
  }

  public boolean contains(List<String> k) {
    return values.contains(mapToSingleKey(k));
  }

  public Set<String> asSingleKeySet() {
    if (keyCount != 1) {
      throw new UnsupportedOperationException();
    }
    return Collections.unmodifiableSet(values);
  }

  private String mapToSingleKey(List<String> k) {
    if (k.size() != keyCount) {
      throw new IllegalArgumentException("Expected exactly " + keyCount + " keys");
    }
    return String.join("\0", k);
  }
}
