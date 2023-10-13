package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record TaxAndLegalReferenceTO(

  @NotBlank
  @Size(max = 50)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$", message = "The Tax and Legal Reference Type cannot start nor end with whitespace characters")
  String type,

  @NotBlank
  @Size(max = 2)
  @Pattern(regexp = "^[A-Z]{2}$", message = "A country code must consist of exactly 2 characters from A-Z")
  String countryCode,

  @NotBlank @Size(max = 100)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$", message = "The Tax and Legal Reference Value cannot contain whitespace characters")
  String value
) {
  @Builder
  public TaxAndLegalReferenceTO { }
}
