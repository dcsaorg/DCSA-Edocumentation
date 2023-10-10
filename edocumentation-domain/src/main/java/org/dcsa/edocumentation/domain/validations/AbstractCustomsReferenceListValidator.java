package org.dcsa.edocumentation.domain.validations;


import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCustomsReferenceListValidator {

  public List<CustomsReference>  checkReferencesDuplicates(List<CustomsReference> customsReferences) {

    return customsReferences.stream()
      .collect(Collectors.groupingBy(p -> p.getType() + "-" + p.getCountryCode(), Collectors.toList()))
      .values()
      .stream()
      .filter(i -> i.size() > 1)
      .flatMap(Collection::stream)
      .toList();
  }
}
