package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.edocumentation.transferobjects.validation.ValidUtilizedTransportEquipment;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@ValidUtilizedTransportEquipment
@AllOrNone({"cargoGrossVolumeUnit", "cargoGrossVolume"})
public record UtilizedTransportEquipmentTO(

  @NotNull(message = "Equipment is required.")
  EquipmentTO equipment,

  @NotNull(message = "Cargo gross weight is required.")
  Double cargoGrossWeight,

  @NotNull(message = "Cargo gross weight unit is required.")
  WeightUnit cargoGrossWeightUnit,

  Double cargoGrossVolume,
  VolumeUnit cargoGrossVolumeUnit,

  Integer numberOfPackages,

  @NotNull(message = "Is shipper owned is required.")
  Boolean isShipperOwned,

  @Valid
  List<SealTO> seals,

  @Valid
  ActiveReeferSettingsTO activeReeferSettings
) {
  @Builder
  public UtilizedTransportEquipmentTO{}
}
