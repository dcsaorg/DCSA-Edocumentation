package org.dcsa.edocumentation.transferobjects.unofficial;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.ReeferType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public record EquipmentAssignmentTO(
  @NotBlank
  @Size(max = 4)
  String requestedISOEquipmentCode,
  ReeferType requestedReeferType,
  @NotEmpty(message = "equipmentReferences must not be empty.")
  List<@NotBlank @Size(max = 15) String> equipmentReferences

  ) {

  @Builder(toBuilder = true)
  public EquipmentAssignmentTO {}
}
