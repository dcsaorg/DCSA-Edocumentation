package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record CustomsReferenceTO(
    @Size(max = 50)
    @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
    String type,
    @Size(min = 2, max = 2)
    @Pattern(regexp = "^[A-Z]{2}$")
    String countryCode,
    @Size(max = 100)
    @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
    String value
) {
  @Builder(toBuilder = true)
  public CustomsReferenceTO { }
}
