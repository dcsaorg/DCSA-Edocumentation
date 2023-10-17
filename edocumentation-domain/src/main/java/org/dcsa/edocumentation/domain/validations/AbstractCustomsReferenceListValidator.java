package org.dcsa.edocumentation.domain.validations;


import org.dcsa.edocumentation.domain.persistence.entity.CustomsReference;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCustomsReferenceListValidator {

  protected List<CustomsReference>  checkReferencesDuplicates(List<CustomsReference> customsReferences) {

    return customsReferences.stream()
      .collect(Collectors.groupingBy(p -> p.getType() + "-" + p.getCountryCode(), Collectors.toList()))
      .values()
      .stream()
      .filter(i -> i.size() > 1)
      .flatMap(Collection::stream)
      .toList();
  }

  protected void validateCustomsReferences(ValidationState<?> state, List<CustomsReference> customsReferences) {
    if (customsReferences == null) {
      return;
    }
    List<CustomsReference> duplicateCustomsReference = checkReferencesDuplicates(customsReferences);
    if (duplicateCustomsReference.size() >1 ) {
      state.getContext().buildConstraintViolationWithTemplate("The customsreferences contains duplicate combination of Type code and Country code." )
        // Match the TO path
        .addPropertyNode("customsReferences")
        .addConstraintViolation();
      state.invalidate();
    }
  }
}
