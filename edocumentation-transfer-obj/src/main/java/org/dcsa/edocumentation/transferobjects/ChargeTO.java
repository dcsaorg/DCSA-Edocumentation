package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.PaymentTerm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChargeTO(
    @NotBlank(message = "The attribute chargeType is required.")
        @Size(max = 20, message = "The attribute chargeType has a max size of 20.")
        String chargeType,
    @NotNull(message = "The attribute currencyAmount is required.") Float currencyAmount,
    @NotBlank(message = "The attribute chargeType is required.")
        @Size(max = 3, message = "The attribute currencyCode has a max size of 3.")
        String currencyCode,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @NotNull(message = "The attribute paymentTermCode is required.")
        PaymentTerm paymentTermCode,
    @NotBlank(message = "The attribute calculationBasis is required.")
        @Size(max = 50, message = "The attribute calculationBasis has a max size of 50.")
        String calculationBasis,
    @NotNull(message = "The attribute unitPrice is required.") Float unitPrice,
    @NotNull(message = "The attribute quantity is required.") Float quantity) {
  @Builder(toBuilder = true)
  public ChargeTO {}
}
