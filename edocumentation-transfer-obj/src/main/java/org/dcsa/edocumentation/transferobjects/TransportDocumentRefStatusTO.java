package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public record TransportDocumentRefStatusTO(
  @Size(max = 20)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String transportDocumentReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String documentStatus
) {
  @Builder
  public TransportDocumentRefStatusTO {}
}
