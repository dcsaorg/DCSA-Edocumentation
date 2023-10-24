package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.domain.persistence.entity.TaxAndLegalReference;
import org.dcsa.edocumentation.transferobjects.TaxAndLegalReferenceTO;

import java.util.UUID;

@UtilityClass
public class TaxAndLegalReferencesDataFactory {

  public static final String COUNTRY_CODE = "NL";
  public static final String TYPE = "VAT";
  public static final String VALUE = "VAT";

  public TaxAndLegalReference singleTaxAndLegalReference() {
     return TaxAndLegalReference.builder()
       .id(UUID.randomUUID())
       .countryCode(COUNTRY_CODE)
       .type(TYPE)
       .value(VALUE)
       .build();
  }
  public TaxAndLegalReferenceTO singleTaxAndLegalReferenceTO() {
    return TaxAndLegalReferenceTO.builder()
      .countryCode(COUNTRY_CODE)
      .type(TYPE)
      .value(VALUE)
      .build();
  }
}
