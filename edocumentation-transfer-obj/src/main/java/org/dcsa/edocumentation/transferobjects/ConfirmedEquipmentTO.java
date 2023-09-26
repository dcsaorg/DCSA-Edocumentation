package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmedEquipmentTO(
    @NotBlank(message = "The attribute isoEquipmentCode is required.")
        @Size(max = 4, message = "The attribute isoEquipmentCode has a max size of 4.")
        @JsonProperty("ISOEquipmentCode")
        String isoEquipmentCode,

    @Min(1)
        Integer units) {
  @Builder(toBuilder = true)
  public ConfirmedEquipmentTO {}
}
