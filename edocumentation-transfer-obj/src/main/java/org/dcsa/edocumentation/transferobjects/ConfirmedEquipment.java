package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record ConfirmedEquipment(
    @NotBlank(message = "The attribute confirmedEquipmentSizeType is required.")
        @Size(max = 4, message = "The attribute confirmedEquipmentSizeType has a max size of 4.")
        String confirmedEquipmentSizeType,
    @NotBlank(message = "The attribute confirmedEquipmentUnits is required.")
        @Size(max = 3, message = "The attribute confirmedEquipmentUnits has a max size of 3.")
        String confirmedEquipmentUnits) {
  @Builder(toBuilder = true)
  public ConfirmedEquipment {}
}
