package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@AllOrNone({"volume", "volumeUnit"})
public record CargoItemTO(

  @Size(max = 15)
  @NotNull(message = "Equipment reference is required.")
  String equipmentReference,

  @NotNull
  Integer numberOfPackages,

  @NotNull Double weight,

  Double volume,

  @NotNull
  WeightUnit weightUnit,

  VolumeUnit volumeUnit,

  @Size(max = 3)
  @NotNull
  String packageCode,

  @Size(max = 50)
  String packageNameOnBL,

  @Valid
  List<CargoLineItemTO> cargoLineItems
) {
  @Builder
  public CargoItemTO{}
}
