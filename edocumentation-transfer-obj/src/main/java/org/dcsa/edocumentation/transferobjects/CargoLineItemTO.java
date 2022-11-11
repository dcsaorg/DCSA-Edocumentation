package org.dcsa.edocumentation.transferobjects;

import lombok.Builder;

import javax.validation.constraints.NotNull;

public record CargoLineItemTO(

  @NotNull(message = "cargoLineItemID is required.")
  String cargoLineItemID,

  @NotNull(message = "shippingMarks is required.")
  String shippingMarks
) {
  @Builder
  public CargoLineItemTO{}
}
