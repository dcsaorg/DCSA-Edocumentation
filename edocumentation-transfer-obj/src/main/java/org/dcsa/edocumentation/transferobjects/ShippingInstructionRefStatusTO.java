package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record ShippingInstructionRefStatusTO(
  @Size(max = 100)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String shippingInstructionReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  EblDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime shippingInstructionCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime shippingInstructionUpdatedDateTime
) {
  @Builder
  public ShippingInstructionRefStatusTO {}
}
