package org.dcsa.edocumentation.domain.validations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.dcsa.edocumentation.domain.persistence.entity.Party;
import org.dcsa.edocumentation.domain.persistence.entity.TaxAndLegalReference;

import java.util.List;
import java.util.Set;

public class PartyValidator implements ConstraintValidator<PartyValidation, Party> {

  @Override
  public boolean isValid(Party value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    var state = ValidationState.of(value, context);
    validateTaxAndLegalReferences(state);
    return state.isValid();
  }

  private void validateTaxAndLegalReferences(ValidationState<Party> state) {
    Set<TaxAndLegalReference> taxAndLegalReferenceSet = state.getValue().getTaxAndLegalReferences();

//    return customsReferences.stream()
//      .collect(Collectors.groupingBy(p -> p.getType() + "-" + p.getCountryCode(), Collectors.toList()))
//      .values()
//      .stream()
//      .filter(i -> i.size() > 1)
//      .flatMap(Collection::stream)
//      .toList();


    for (TaxAndLegalReference taxAndLegalReference : taxAndLegalReferenceSet) {
      List<TaxAndLegalReference> matched = taxAndLegalReferenceSet.stream()
        .filter(inner -> inner.getType().equals(taxAndLegalReference.getType())
          && inner.getCountryCode().equals(taxAndLegalReference.getCountryCode()))
        .toList();

      if (matched.size() > 1) {
        TaxAndLegalReference illegalTaxAndLegalReference = matched.get(0);
        state.getContext().buildConstraintViolationWithTemplate(
            "Tax and Legal Reference Type = " + illegalTaxAndLegalReference.getType() + " and country code = " + illegalTaxAndLegalReference.getCountryCode() +
              " can only appear once for a Party. Please correct party: " + state.getValue().toString())
          .addConstraintViolation();
        state.invalidate();
        return;
      }
    }
  }
}
