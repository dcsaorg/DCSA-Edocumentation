package org.dcsa.edocumentation.transferobjects.unofficial;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.EblDocumentStatus;

import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record TransportDocumentRefStatusTO(
  @Size(max = 100)
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  String transportDocumentReference,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  EblDocumentStatus documentStatus,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime transportDocumentCreatedDateTime,

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  OffsetDateTime transportDocumentUpdatedDateTime
) {
  @Builder
  public TransportDocumentRefStatusTO {}
}
