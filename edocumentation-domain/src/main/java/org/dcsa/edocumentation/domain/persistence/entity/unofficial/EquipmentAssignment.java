package org.dcsa.edocumentation.domain.persistence.entity.unofficial;

import org.dcsa.edocumentation.domain.persistence.entity.Equipment;
import org.dcsa.edocumentation.domain.persistence.entity.enums.ReeferType;

import java.util.List;

public record EquipmentAssignment(
  String requestedISOEquipmentCode,
  ReeferType requestedReeferType,
  List<Equipment> equipments

) {

  public String getMatchKey() {
    String reeferKey = "<NONE>";
    if (requestedReeferType != null) {
      reeferKey = requestedReeferType.name();
    }
    return requestedISOEquipmentCode + "/" + reeferKey;
  }
}
