package org.dcsa.edocumentation.domain.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dcsa.edocumentation.domain.persistence.entity.enums.CutOffDateTimeCode;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ShipmentCutOffTimeId implements Serializable {
  private UUID shipmentID;

  private CutOffDateTimeCode cutOffDateTimeCode;

}
