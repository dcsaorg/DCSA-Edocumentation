package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.validation.TaxAndLegalReferenceValidation;

@TaxAndLegalReferenceValidation
public record TaxAndLegalReferenceTO(

  @NotBlank
  @Size(max = 50)
  @Pattern(regexp = "^\\S+$", message = "The Tax and Legal Reference Type cannot start nor end with whitespace characters")
  String type,

  @NotBlank
  @Size(max = 2)
  @Pattern(regexp = "^[A-Z]{2}$", message = "Not a valid country code")
  String countryCode,

  @NotBlank @Size(max = 100)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$", message = "The Tax and Legal Reference Value cannot contain whitespace characters")
  String value

  @Builder(toBuilder = true)
  public TaxAndLegalReferenceTO {}
}
