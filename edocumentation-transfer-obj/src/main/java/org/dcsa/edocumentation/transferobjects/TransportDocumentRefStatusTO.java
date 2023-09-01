package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;

public record TransportDocumentRefStatusTO(
  @Size(max = 20)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String transportDocumentReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  EblDocumentStatus documentStatus
) {
  @Builder
  public TransportDocumentRefStatusTO {}
}
