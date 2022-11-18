package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.ConsignmentItemTO;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

@UtilityClass
public class ConsignmentItemDataFactory {

  public ConsignmentItemTO singleConsignmentItem() {

    return ConsignmentItemTO.builder()
        .cargoItems(CargoItemDataFactory.multipleCargoItems())
        .carrierBookingReference("CBR_01")
        .hsCode("41150")
        .weightUnit(WeightUnit.KGM)
        .weight(10.23)
        .volume(12034.3)
        .volumeUnit(VolumeUnit.MTQ)
        .build();
  }
}
