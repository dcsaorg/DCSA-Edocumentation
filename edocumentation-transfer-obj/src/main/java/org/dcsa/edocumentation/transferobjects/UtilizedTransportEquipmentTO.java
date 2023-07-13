package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@DisallowIfBoolean(ifField = "isNonOperatingReefer", hasValue = true, thenDisallow = "activeReeferSettings")
@ValidUtilizedTransportEquipment
@AllOrNone({"cargoGrossVolumeUnit", "cargoGrossVolume"})
// TODO: Split into a Shipper provided and a Carrier provided version
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

  // Carrier Provided only; must not be present in the shipper provided version
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  Boolean isNonOperatingReefer,

  // Carrier Provided only; must not be present in the shipper provided version
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)

  ActiveReeferSettingsTO activeReeferSettings,

  String equipmentReference,
  @Valid
  EquipmentTO equipment,

  @Valid
  List<@Valid @NotNull SealTO> seals,

  @Valid
  List<@Valid @NotNull ReferenceTO> references,

  @Valid
  List<@Valid @NotNull CustomsReferenceTO> customsReferences
) {
  @Builder
  public UtilizedTransportEquipmentTO{}

  public String extractEquipmentReference() {
    return isShipperOwned ? equipment.equipmentReference() : equipmentReference;
  }
}
