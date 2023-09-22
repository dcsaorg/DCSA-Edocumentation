package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.validation.AdvanceManifestFilingValidation;

@AdvanceManifestFilingValidation
public record AdvanceManifestFilingTO(

  @NotNull
  @Size(max = 3)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$", message = "Not a valid manifest type code ")
  String manifestTypeCode,
  @NotNull
  @Size(max = 2)
  @Pattern(regexp = "^[A-Z]{2}$", message = "Not a valid country code")
  String countryCode) {
  @Builder(toBuilder = true)
  public AdvanceManifestFilingTO {}
}
