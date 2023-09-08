package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder(toBuilder = true)
public record LocationTO(
  @Size(max = 100)
  String locationName,

  @NotNull
  @Size(max = 4)
  String locationType,

  @Valid
  AddressTO address,

  @Size(max = 5)
  @JsonProperty("UNLocationCode")
  String UNLocationCode,

  @Size(max = 6)
  String facilityCode,

  @Size(max = 4)
  String facilityCodeListProvider
) {

}
