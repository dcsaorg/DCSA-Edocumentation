package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public record RequestedEquipmentTO(
  @NotBlank @Size(max = 4)
  String sizeType,

  @NotNull
  Integer units,

  List<@NotBlank @Size(max = 15) String> equipmentReferences,

  @NotNull
  Boolean isShipperOwned
) {
  @Builder(toBuilder = true)
  public RequestedEquipmentTO { }
}
