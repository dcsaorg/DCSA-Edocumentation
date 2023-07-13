package org.dcsa.edocumentation.datafactories;

import lombok.experimental.UtilityClass;
import org.dcsa.edocumentation.transferobjects.CargoItemTO;
import org.dcsa.edocumentation.transferobjects.CargoLineItemTO;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;

import java.util.List;

@UtilityClass
public class CargoItemDataFactory {
  public CargoItemTO singleCargoItem() {
    return CargoItemTO.builder()
        .cargoLineItems(
            List.of(
                CargoLineItemTO.builder()
                    .shippingMarks("shipping marks")
                    .cargoLineItemID("CLI_ID_01")
                    .build()))
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
            .cargoLineItems(
                List.of(
                    CargoLineItemTO.builder()
                        .shippingMarks("shipping marks")
                        .cargoLineItemID("CLI_ID_01")
                        .build()))
            .equipmentReference("CARR_EQ_REF_02")
            .weight(10.2)
            .weightUnit(WeightUnit.KGM)
            .volume(234234.3)
            .volumeUnit(VolumeUnit.MTQ)
            .build());
  }
}
