package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.AdvanceManifestFilingsHouseBLPerformedBy;

public record AdvanceManifestFilingEBLTO(

  @NotNull
  @Size(max = 50)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String manifestTypeCode,
  @NotNull
  @Size(max = 2)
  @Pattern(regexp = "^[A-Z]{2}$")
  String countryCode,
  @NotNull
  AdvanceManifestFilingsHouseBLPerformedBy advanceManifestFilingsPerformedBy,

  @Size(max = 3)
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String selfFilerCode ) {
  @Builder(toBuilder = true)
  public AdvanceManifestFilingEBLTO {}
}
