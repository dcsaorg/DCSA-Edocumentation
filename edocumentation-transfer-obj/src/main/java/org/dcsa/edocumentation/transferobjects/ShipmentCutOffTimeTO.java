package org.dcsa.edocumentation.transferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.dcsa.edocumentation.transferobjects.enums.CutOffDateTimeCode;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

public record ShipmentCutOffTimeTO(
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute cutOffDateTimeCode is required.")
        CutOffDateTimeCode cutOffDateTimeCode,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull(message = "The attribute cutOffDateTime is required.")
        OffsetDateTime cutOffDateTime) {

  @Builder(toBuilder = true)
  public ShipmentCutOffTimeTO {}
}
