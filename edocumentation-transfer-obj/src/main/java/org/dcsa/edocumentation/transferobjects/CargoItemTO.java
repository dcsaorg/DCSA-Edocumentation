package org.dcsa.edocumentation.transferobjects;

import jakarta.validation.constraints.Pattern;
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
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  String equipmentReference,

  @NotNull Double weight,

  Double volume,

  @NotNull
  WeightUnit weightUnit,

  VolumeUnit volumeUnit,

  // TODO: Missing outerPackaging (DG)

  // TODO: cargoLineItems have been replaced by shipping marks.
  @Valid
  List<CargoLineItemTO> cargoLineItems
) {
  @Builder
  public CargoItemTO{}
}
