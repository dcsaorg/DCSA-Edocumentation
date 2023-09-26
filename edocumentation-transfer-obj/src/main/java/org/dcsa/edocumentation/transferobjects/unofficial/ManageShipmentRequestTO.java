package org.dcsa.edocumentation.transferobjects.unofficial;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.AdvanceManifestFilingTO;
import org.dcsa.edocumentation.transferobjects.ConfirmedEquipmentTO;
import org.dcsa.edocumentation.transferobjects.ShipmentCutOffTimeTO;
import org.dcsa.edocumentation.transferobjects.ShipmentLocationTO;
import org.dcsa.edocumentation.transferobjects.TransportTO;

public record ManageShipmentRequestTO(
  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  @Size(max = 35, message = "The attribute carrierBookingReference has a max size of 35.")
  String carrierBookingReference,

  @Pattern(regexp = "^\\S+(\\s+\\S+)*$")
  @Size(max = 100)
  @NotBlank
  String carrierBookingRequestReference,

  String termsAndConditions,

  @Size(max = 4)
  @NotNull
  String carrierSMDGCode,

  List<@Valid ConfirmedEquipmentTO> confirmedEquipments,

  @Valid List<@Valid ShipmentLocationTO> shipmentLocations,

  @Valid List<@Valid TransportTO> transports,

  List<@Valid ShipmentCutOffTimeTO> shipmentCutOffTimes,

  List<@Valid AdvanceManifestFilingTO> advanceManifestFiling
  ) {

  @Builder(toBuilder = true)
  public ManageShipmentRequestTO {}
}
