package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.skernel.infrastructure.validation.RequiredIfOther;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@RequiredIfOther(ifNotNull = "volume", thenNotNull = "volumeUnit")
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

  @Valid
  List<CargoLineItemTO> cargoLineItems
) {
  @Builder
  public CargoItemTO{}
}
