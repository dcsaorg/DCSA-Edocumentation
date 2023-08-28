package org.dcsa.edocumentation.datafactories;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

@UtilityClass
public class CargoItemDataFactory {
  public CargoItemTO singleCargoItem() {
    return CargoItemTO.builder()
        .shippingMarks(List.of("shipping marks"))
        .equipmentReference("CARR_EQ_REF_01")
        .weight(10.2)
        .weightUnit(WeightUnit.KGM)
        .volume(234234.3)
        .volumeUnit(VolumeUnit.MTQ)
        .build();
  }

  public List<CargoItemTO> multipleCargoItems() {
    return List.of(
        singleCargoItem(),
        CargoItemTO.builder()
            .shippingMarks(List.of("shipping marks"))
            .equipmentReference("CARR_EQ_REF_02")
            .weight(10.2)
            .weightUnit(WeightUnit.KGM)
            .volume(234234.3)
            .volumeUnit(VolumeUnit.MTQ)
            .build());
  }
}
