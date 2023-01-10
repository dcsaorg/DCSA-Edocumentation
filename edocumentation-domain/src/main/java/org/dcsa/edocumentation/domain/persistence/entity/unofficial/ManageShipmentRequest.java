package org.dcsa.edocumentation.domain.persistence.entity.unofficial;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

// Record because it is never persisted
public record ManageShipmentRequest(
  @NotBlank
  @Size(max = 35, message = "The attribute carrierBookingReference has a max size of 35.")
  String carrierBookingReference,

  @NotBlank
  @Size(max = 100)
  String carrierBookingRequestReference,

  String termsAndConditions,

  @Size(max = 4)
  @NotNull
  String carrierSMDGCode,

  @Valid List<@Valid EquipmentAssignment> equipmentAssignments
) {
}
