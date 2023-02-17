package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.VolumeUnit;
import org.dcsa.edocumentation.transferobjects.enums.WeightUnit;
import org.dcsa.edocumentation.transferobjects.validation.ValidUtilizedTransportEquipment;
import org.dcsa.skernel.infrastructure.validation.AllOrNone;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.dcsa.skernel.infrastructure.validation.DisallowIfBoolean;
import org.dcsa.skernel.infrastructure.validation.RequiredIfFalse;
import org.dcsa.skernel.infrastructure.validation.RequiredIfTrue;

import java.util.List;

@RequiredIfTrue(isFieldReferenceRequired = "isShipperOwned", fieldReference = "equipment")
@DisallowIfBoolean(ifField = "isShipperOwned", hasValue = false, thenDisallow = "equipment")
@RequiredIfFalse(ifFalse = "isShipperOwned", thenNotNull = "equipmentReference")
@DisallowIfBoolean(ifField = "isShipperOwned", hasValue = true, thenDisallow = "equipmentReference")
@ValidUtilizedTransportEquipment
@AllOrNone({"cargoGrossVolumeUnit", "cargoGrossVolume"})
public record UtilizedTransportEquipmentTO(

  @NotNull(message = "Cargo gross weight is required.")
  Double cargoGrossWeight,

  @NotNull(message = "Cargo gross weight unit is required.")
  WeightUnit cargoGrossWeightUnit,

  Double cargoGrossVolume,
  VolumeUnit cargoGrossVolumeUnit,

  Integer numberOfPackages,

  @NotNull(message = "Is shipper owned is required.")
  Boolean isShipperOwned,

  String equipmentReference,
  @Valid
  EquipmentTO equipment,

  @Valid
  List<SealTO> seals
) {
  @Builder
  public UtilizedTransportEquipmentTO{}

  public String extractEquipmentReference() {
    return isShipperOwned ? equipment.equipmentReference() : equipmentReference;
  }
}
